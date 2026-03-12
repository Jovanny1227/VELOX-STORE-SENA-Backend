package com.sena.tienda.controller;

import com.sena.tienda.model.Venta;
import com.sena.tienda.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLADOR: VentaController
 * Expone los endpoints REST para registrar y consultar ventas.
 */
@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // =========================================
    // REGISTRAR VENTA
    // =========================================
    @PostMapping("/registrar")
    public Venta registrarVenta(
            @RequestParam Long clienteId,
            @RequestParam String codigoBicicleta,
            @RequestParam int cantidad) {

        return ventaService.registrarVenta(clienteId, codigoBicicleta, cantidad);
    }

    // =========================================
    // LISTAR TODAS LAS VENTAS
    // =========================================
    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listarTodasLasVentas();
    }

    // =========================================
    // BUSCAR VENTAS POR CLIENTE
    // =========================================
    @GetMapping("/cliente/{clienteId}")
    public List<Venta> ventasPorCliente(@PathVariable Long clienteId) {
        return ventaService.buscarVentasPorCliente(clienteId);
    }

    // =========================================
    // BUSCAR VENTA POR ID
    // =========================================
    @GetMapping("/{idVenta}")
    public Venta buscarVenta(@PathVariable Long idVenta) {
        return ventaService.buscarVentaPorId(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
}