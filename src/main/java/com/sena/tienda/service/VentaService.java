package com.sena.tienda.service;

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
    private final ClienteRepository clienteRepository;
    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioService movimientoService;

    public VentaService(VentaRepository ventaRepository, ClienteRepository clienteRepository,
                        BicicletaRepository bicicletaRepository, InventarioRepository inventarioRepository,
                        MovimientoInventarioService movimientoService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoService = movimientoService;
    }

    @Transactional
    public Venta registrarVenta(Long clienteId, String codigoBicicleta, int cantidad) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        Bicicleta bicicleta = bicicletaRepository.findByCodigo(codigoBicicleta)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + codigoBicicleta));
        if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser mayor a cero");
        MovimientoRequest movReq = new MovimientoRequest();
        movReq.setCodigoBicicleta(codigoBicicleta);
        movReq.setCantidad(cantidad);
        movReq.setTipo(TipoMovimiento.SALIDA_VENTA);
        movReq.setObservacion("Venta a cliente: " + cliente.getNombre());
        movimientoService.registrar(movReq);
        Venta venta = new Venta(cliente);
        DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
        venta.getDetalles().add(detalle);
        venta.setTotal(detalle.getSubtotal());
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta registrarVentaMultiple(Long clienteId, List<VentaRequest.ItemVentaRequest> items) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        if (items == null || items.isEmpty())
            throw new RuntimeException("Debe incluir al menos una bicicleta");
        Venta venta = new Venta(cliente);
        BigDecimal totalVenta = BigDecimal.ZERO;
        for (VentaRequest.ItemVentaRequest item : items) {
            if (item.getCantidad() <= 0) throw new RuntimeException("Cantidad invalida");
            Bicicleta bicicleta = bicicletaRepository.findByCodigo(item.getCodigoBicicleta())
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + item.getCodigoBicicleta()));
            MovimientoRequest movReq = new MovimientoRequest();
            movReq.setCodigoBicicleta(item.getCodigoBicicleta());
            movReq.setCantidad(item.getCantidad());
            movReq.setTipo(TipoMovimiento.SALIDA_VENTA);
            movReq.setObservacion("Venta multiple a: " + cliente.getNombre());
            movimientoService.registrar(movReq);
            DetalleVenta detalle = new DetalleVenta(venta, bicicleta, item.getCantidad());
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
            Inventario inventario = inventarioRepository
                    .findByBicicletaIdBicicleta(detalle.getBicicleta().getIdBicicleta())
                    .orElse(null);
            if (inventario != null) {
                inventario.setCantidadDisponible(inventario.getCantidadDisponible() + detalle.getCantidad());
                inventarioRepository.save(inventario);
            }
        }
        ventaRepository.delete(venta);
    }

    public List<Venta> listarTodasLasVentas() { return ventaRepository.findAll(); }

    public Optional<Venta> buscarVentaPorId(Long idVenta) { return ventaRepository.findById(idVenta); }
}
