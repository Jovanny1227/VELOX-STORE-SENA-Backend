package com.sena.tienda.service;

import com.sena.tienda.dto.request.VentaPresencialRequest;
import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import com.sena.tienda.dto.request.MovimientoRequest;
import com.sena.tienda.dto.request.VentaRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioService movimientoService;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository, UsuarioRepository usuarioRepository,
                        BicicletaRepository bicicletaRepository, InventarioRepository inventarioRepository,
                        MovimientoInventarioService movimientoService,
                        ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoService = movimientoService;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Venta registrarVenta(Long usuarioId, String codigoBicicleta, int cantidad) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        Bicicleta bicicleta = bicicletaRepository.findByCodigo(codigoBicicleta)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + codigoBicicleta));

        if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser mayor a cero");

        Inventario inventario = inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                .orElseGet(() -> new Inventario(bicicleta, 0));

        if (inventario.getCantidadDisponible() < cantidad) {
            int stockEnCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;

            if (stockEnCatalogo >= cantidad) {
                int faltante = stockEnCatalogo - inventario.getCantidadDisponible();
                MovimientoRequest ajusteReq = new MovimientoRequest();
                ajusteReq.setCodigoBicicleta(bicicleta.getCodigo());
                ajusteReq.setCantidad(faltante);
                ajusteReq.setTipo(TipoMovimiento.AJUSTE_POSITIVO);
                ajusteReq.setObservacion("Ajuste automático por desincronización de inventario inicial");
                movimientoService.registrar(ajusteReq);
                inventario.setCantidadDisponible(inventario.getCantidadDisponible() + faltante);
            } else {
                throw new RuntimeException("Stock insuficiente real para: " + bicicleta.getModelo() + ". Solo hay " + inventario.getCantidadDisponible());
            }
        }

        int stockCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : inventario.getCantidadDisponible();
        bicicleta.setStock(stockCatalogo - cantidad);
        bicicletaRepository.save(bicicleta);

        MovimientoRequest movReq = new MovimientoRequest();
        movReq.setCodigoBicicleta(codigoBicicleta);
        movReq.setCantidad(cantidad);
        movReq.setTipo(TipoMovimiento.SALIDA_VENTA);
        movReq.setObservacion("Venta a: " + usuario.getNombre());
        movimientoService.registrar(movReq);

        Venta venta = new Venta(usuario);
        DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
        venta.getDetalles().add(detalle);
        venta.setTotal(detalle.getSubtotal());

        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta registrarVentaMultiple(Long usuarioId, VentaPresencialRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        if (request.getItems() == null || request.getItems().isEmpty())
            throw new RuntimeException("Debe incluir al menos una bicicleta");

        Venta venta = new Venta(usuario);
        venta.setTipoVenta(request.getTipoVenta() != null ? request.getTipoVenta() : TipoVenta.VIRTUAL);

        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            venta.setCliente(cliente);
        }

        BigDecimal totalVenta = BigDecimal.ZERO;
        String nombreComprador = (venta.getCliente() != null) ? venta.getCliente().getNombre() : usuario.getNombre();
        String textoObservacion = "Venta " + venta.getTipoVenta().name() + " a: " + nombreComprador;

        for (VentaRequest.ItemVentaRequest item : request.getItems()) {
            Bicicleta bicicleta = bicicletaRepository.findByCodigo(item.getCodigoBicicleta())
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + item.getCodigoBicicleta()));

            int cantidad = item.getCantidad();
            if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser mayor a cero");

            Inventario inventario = inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                    .orElseGet(() -> new Inventario(bicicleta, 0));

            // Validar y descontar stock real
            if (inventario.getCantidadDisponible() < cantidad) {
                int stockEnCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;
                if (stockEnCatalogo >= cantidad) {
                    int faltante = stockEnCatalogo - inventario.getCantidadDisponible();
                    MovimientoRequest ajusteReq = new MovimientoRequest();
                    ajusteReq.setCodigoBicicleta(bicicleta.getCodigo());
                    ajusteReq.setCantidad(faltante);
                    ajusteReq.setTipo(TipoMovimiento.AJUSTE_POSITIVO);
                    ajusteReq.setObservacion("Ajuste automático por desincronización");
                    movimientoService.registrar(ajusteReq);
                    inventario.setCantidadDisponible(inventario.getCantidadDisponible() + faltante);
                } else {
                    throw new RuntimeException("Stock insuficiente para: " + bicicleta.getModelo());
                }
            }

            // 1. Sincronizar catálogo visual (Bicicleta)
            int stockCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : inventario.getCantidadDisponible();
            bicicleta.setStock(stockCatalogo - cantidad);
            bicicletaRepository.save(bicicleta);

            // 2. Registrar Salida oficial en el Kardex
            MovimientoRequest movReq = new MovimientoRequest();
            movReq.setCodigoBicicleta(item.getCodigoBicicleta());
            movReq.setCantidad(item.getCantidad());
            movReq.setTipo(TipoMovimiento.SALIDA_VENTA);
            movReq.setObservacion(textoObservacion);
            movimientoService.registrar(movReq);

            // 3. Crear el detalle y sumarlo a la factura
            DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
            venta.getDetalles().add(detalle);
            totalVenta = totalVenta.add(detalle.getSubtotal());
        }

        venta.setTotal(totalVenta);
        return ventaRepository.save(venta);
    }

    @Transactional
    public void eliminarVenta(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + idVenta));

        for (DetalleVenta detalle : venta.getDetalles()) {
            Bicicleta bicicleta = detalle.getBicicleta();

            Inventario inventario = inventarioRepository
                    .findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                    .orElse(null);

            if (inventario != null) {
                inventario.setCantidadDisponible(inventario.getCantidadDisponible() + detalle.getCantidad());
                inventarioRepository.save(inventario);
            }

            int stockActualEliminar = (bicicleta.getStock() != null) ? bicicleta.getStock() :
                    (inventario != null ? inventario.getCantidadDisponible() - detalle.getCantidad() : 0);
            bicicleta.setStock(stockActualEliminar + detalle.getCantidad());
            bicicletaRepository.save(bicicleta);
        }
        ventaRepository.delete(venta);
    }

    @Transactional(readOnly = true)
    public List<Venta> listarTodasLasVentas() { return ventaRepository.findAll(); }

    public Optional<Venta> buscarVentaPorId(Long idVenta) { return ventaRepository.findById(idVenta); }
}
