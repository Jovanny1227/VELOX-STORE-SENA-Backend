package com.sena.tienda.controller;

import com.sena.tienda.dto.VentaRequest;
import com.sena.tienda.exception.RecursoNoEncontradoException;
import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.model.Cliente;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.BicicletaService;
import com.sena.tienda.service.ClienteService;
import com.sena.tienda.service.VentaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Inyección de dependencias por constructor (Cumpliendo DIP)
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final BicicletaService bicicletaService;

    public VentaController(VentaService ventaService, ClienteService clienteService, BicicletaService bicicletaService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.bicicletaService = bicicletaService;
    }

    // =========================================
    // REGISTRAR VENTA
    // =========================================
    @PostMapping("/registrar")
    public ResponseEntity<Venta> registrarVenta(@RequestBody VentaRequest request) {

        // 1. El controlador orquesta la búsqueda de las entidades
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + request.getClienteId()));

        Bicicleta bicicleta = bicicletaService.buscarPorCodigo(request.getCodigoBicicleta())
                .orElseThrow(() -> new RecursoNoEncontradoException("Bicicleta no encontrada con código: " + request.getCodigoBicicleta()));

        // 2. Le pasa los objetos completos al servicio
        Venta nuevaVenta = ventaService.registrarVenta(cliente, bicicleta, request.getCantidad());

        // 3. Devuelve 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    // =========================================
    // LISTAR TODAS LAS VENTAS
    // =========================================
    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodasLasVentas());
    }

    // =========================================
    // BUSCAR VENTAS POR CLIENTE
    // =========================================
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> ventasPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ventaService.buscarVentasPorCliente(clienteId));
    }

    // =========================================
    // BUSCAR VENTA POR ID
    // =========================================
    @GetMapping("/{idVenta}")
    public ResponseEntity<Venta> buscarVenta(@PathVariable Long idVenta) {
        Venta venta = ventaService.buscarVentaPorId(idVenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + idVenta));
        return ResponseEntity.ok(venta);
    }
}