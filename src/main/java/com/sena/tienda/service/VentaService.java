package com.sena.tienda.service;

import com.sena.tienda.dto.request.MovimientoRequest;
import com.sena.tienda.dto.request.VentaPresencialRequest;
import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
    public Venta registrarVentaMultiple(Long usuarioId, VentaPresencialRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Debe incluir al menos una bicicleta");
        }

        Venta venta = new Venta(usuario);
        venta.setTipoVenta(request.getTipoVenta() != null ? request.getTipoVenta() : TipoVenta.PRESENCIAL);

        // AQUÍ ESTÁ LA MAGIA: Conectamos la venta con el cliente si viene en la petición
        if (request.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getClienteId()));
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

            if (inventario.getCantidadDisponible() < cantidad) {
                int stockEnCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;
                if (stockEnCatalogo >= cantidad) {
                    int faltante = stockEnCatalogo - inventario.getCantidadDisponible();
                    MovimientoRequest ajusteReq = new MovimientoRequest();
                    ajusteReq.setCodigoBicicleta(bicicleta.getCodigo());
                    ajusteReq.setCantidad(faltante);
                    ajusteReq.setTipo(TipoMovimiento.AJUSTE_POSITIVO);
                    ajusteReq.setObservacion("Ajuste automático");
                    movimientoService.registrar(ajusteReq);
                    inventario.setCantidadDisponible(inventario.getCantidadDisponible() + faltante);
                } else {
                    throw new RuntimeException("Stock insuficiente real para: " + bicicleta.getModelo());
                }
            }

            // Descontar inventario y stock general
            int stockCatalogo = (bicicleta.getStock() != null) ? bicicleta.getStock() : inventario.getCantidadDisponible();
            bicicleta.setStock(stockCatalogo - cantidad);
            bicicletaRepository.save(bicicleta);

            // Registrar movimiento de salida con el nombre del comprador real
            MovimientoRequest movReq = new MovimientoRequest();
            movReq.setCodigoBicicleta(bicicleta.getCodigo());
            movReq.setCantidad(cantidad);
            movReq.setTipo(TipoMovimiento.SALIDA_VENTA);
            movReq.setObservacion(textoObservacion);
            movimientoService.registrar(movReq);

            // Crear el detalle
            DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
            venta.getDetalles().add(detalle);
            totalVenta = totalVenta.add(detalle.getSubtotal());
        }

        venta.setTotal(totalVenta);
        return ventaRepository.save(venta);
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }
}
