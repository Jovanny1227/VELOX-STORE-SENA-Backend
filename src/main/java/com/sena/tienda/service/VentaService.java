package com.sena.tienda.service;

import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private BicicletaRepository bicicletaRepository;
    @Autowired private InventarioRepository inventarioRepository;

    @Transactional
    public Venta registrarVenta(Long clienteId, String codigoBicicleta, int cantidad) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        Bicicleta bicicleta = bicicletaRepository.findByCodigo(codigoBicicleta)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + codigoBicicleta));
        Inventario inventario = inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para: " + codigoBicicleta));
        if (cantidad <= 0) throw new RuntimeException("La cantidad debe ser mayor a cero");
        if (inventario.getCantidadDisponible() < cantidad)
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventario.getCantidadDisponible());
        Venta venta = new Venta(cliente);
        venta.setFecha(LocalDateTime.now());
        DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
        venta.getDetalles().add(detalle);
        venta.setTotal(detalle.getSubtotal());
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);
        inventarioRepository.save(inventario);
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

    public List<Venta> listarTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public List<Venta> buscarVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteClienteId(clienteId);
    }

    public Optional<Venta> buscarVentaPorId(Long idVenta) {
        return ventaRepository.findById(idVenta);
    }
}