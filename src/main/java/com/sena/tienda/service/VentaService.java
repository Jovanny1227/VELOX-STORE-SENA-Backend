package com.sena.tienda.service;

import com.sena.tienda.exception.RecursoNoEncontradoException;
import com.sena.tienda.exception.StockInsuficienteException;
import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SERVICIO: VentaService
 * Lógica de negocio para el registro de ventas.
 */
@Service
public class VentaService {

    // 1. DEPENDENCIAS INMUTABLES (DIP: Inyección por constructor)
    private final VentaRepository ventaRepository;
    private final InventarioRepository inventarioRepository;

    public VentaService(VentaRepository ventaRepository, InventarioRepository inventarioRepository) {
        this.ventaRepository = ventaRepository;
        this.inventarioRepository = inventarioRepository;
    }

    // ===================================================================
    // REGISTRAR VENTA
    // ===================================================================

    /**
     * Registra una venta completa utilizando los objetos de dominio.
     *
     * @param cliente   Objeto Cliente (Usuario) que realiza la compra
     * @param bicicleta Objeto Bicicleta que se va a vender
     * @param cantidad  Unidades a comprar
     * @return la Venta persistida
     */
    @Transactional
    public Venta registrarVenta(Cliente cliente, Bicicleta bicicleta, int cantidad) {

        // ---- VALIDACIÓN 1: Integridad de los objetos ----
        if (cliente == null || cliente.getClienteId() == null) {
            throw new RecursoNoEncontradoException("El objeto Cliente proporcionado no es válido.");
        }
        if (bicicleta == null || bicicleta.getIdBicicleta() == null) {
            throw new RecursoNoEncontradoException("El objeto Bicicleta proporcionado no es válido.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de venta debe ser mayor a cero.");
        }

        // ---- VALIDACIÓN 2: Stock en Inventario (Uso de excepciones propias) ----
        Inventario inventario = inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay registro de inventario para la bicicleta '" + bicicleta.getCodigo() + "'."));

        if (inventario.getCantidadDisponible() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente. Disponible: " + inventario.getCantidadDisponible() +
                            " | Solicitado: " + cantidad);
        }

        // ---- CREACIÓN DE LA VENTA (cabecera) ----
        Venta venta = new Venta(cliente);
        venta.setFecha(LocalDateTime.now());

        // ---- CREACIÓN DEL DETALLE ----
        DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
        venta.getDetalles().add(detalle);

        // ---- CÁLCULO DEL TOTAL ----
        venta.setTotal(detalle.getSubtotal());

        // ---- DESCUENTO AUTOMÁTICO DE STOCK ----
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);
        inventarioRepository.save(inventario);

        // ---- PERSISTENCIA ----
        Venta ventaGuardada = ventaRepository.save(venta);

        System.out.println("✅ Venta registrada. id_venta=" + ventaGuardada.getIdVenta()
                + " | Total=$" + ventaGuardada.getTotal());

        return ventaGuardada;
    }

    // ===================================================================
    // CONSULTAS
    // ===================================================================

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