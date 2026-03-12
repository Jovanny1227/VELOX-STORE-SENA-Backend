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

/**
 * SERVICIO: VentaService
 * Lógica de negocio para el registro de ventas.
 *
 * Validaciones (con if):
 *   1. Que el cliente exista por su cliente_id (Long)
 *   2. Que la bicicleta exista por su codigo
 *   3. Que el stock en Inventario sea suficiente
 *
 * Cálculos:
 *   subtotal = precio × cantidad  (en DetalleVenta)
 *   total    = suma de subtotales  (en Venta)
 *   Descuenta automáticamente del stock en Inventario
 */
@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    // ===================================================================
    // REGISTRAR VENTA
    // ===================================================================

    /**
     * Registra una venta completa.
     *
     * @param clienteId       cliente_id del cliente (Long, PK de la tabla Clientes)
     * @param codigoBicicleta código único de la bicicleta
     * @param cantidad        unidades a comprar
     * @return la Venta persistida con su id_venta generado
     */
    @Transactional
    public Venta registrarVenta(Long clienteId, String codigoBicicleta, int cantidad) {

        // ---- VALIDACIÓN 1: Existencia del Cliente (por cliente_id) ----
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);

        if (!clienteOpt.isPresent()) {
            throw new RuntimeException(
                    "ERROR: No existe un cliente con cliente_id = " + clienteId);
        }
        Cliente cliente = clienteOpt.get();

        // ---- VALIDACIÓN 2: Existencia de la Bicicleta (por codigo) ----
        Optional<Bicicleta> bicicletaOpt = bicicletaRepository.findByCodigo(codigoBicicleta);

        if (!bicicletaOpt.isPresent()) {
            throw new RuntimeException(
                    "ERROR: No existe bicicleta con código '" + codigoBicicleta + "'.");
        }
        Bicicleta bicicleta = bicicletaOpt.get();

        // ---- VALIDACIÓN 3: Stock en Inventario ----
        Optional<Inventario> inventarioOpt =
                inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta());

        if (!inventarioOpt.isPresent()) {
            throw new RuntimeException(
                    "ERROR: No hay registro de inventario para la bicicleta '" + codigoBicicleta + "'.");
        }
        Inventario inventario = inventarioOpt.get();

        if (cantidad <= 0) {
            throw new RuntimeException("ERROR: La cantidad debe ser mayor a cero.");
        }

        if (inventario.getCantidadDisponible() < cantidad) {
            throw new RuntimeException(
                    "ERROR: Stock insuficiente. Disponible: " +
                            inventario.getCantidadDisponible() + " | Solicitado: " + cantidad);
        }

        // ---- CREACIÓN DE LA VENTA (cabecera) ----
        Venta venta = new Venta(cliente);
        venta.setFecha(LocalDateTime.now());

        // ---- CREACIÓN DEL DETALLE ----
        // subtotal = precio × cantidad  (calculado en el constructor)
        DetalleVenta detalle = new DetalleVenta(venta, bicicleta, cantidad);
        venta.getDetalles().add(detalle);

        // ---- CÁLCULO DEL TOTAL ----
        venta.setTotal(detalle.getSubtotal());

        // ---- DESCUENTO AUTOMÁTICO DE STOCK ----
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - cantidad);
        inventarioRepository.save(inventario);

        // ---- PERSISTENCIA (cascade guarda también los DetalleVenta) ----
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