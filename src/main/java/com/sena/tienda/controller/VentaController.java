package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaPresencialRequest;
import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<Venta> registrarVenta(@RequestBody VentaRequest request) {
        Venta nuevaVenta = ventaService.registrarVenta(
                request.getUsuarioId(),
                request.getCodigoBicicleta(),
                request.getCantidad()
        );
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    // 🔥 CORRECCIÓN AQUÍ: Cambiamos a VentaPresencialRequest
    @PostMapping("/registrar-multiple")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<Venta> registrarVentaMultiple(@RequestBody VentaPresencialRequest request) {
        Venta nuevaVenta = ventaService.registrarVentaMultiple(
                request.getUsuarioId(), // Tomamos el ID del cajero
                request                 // Le pasamos todo el objeto al servicio
        );
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodasLasVentas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<Venta> buscarVentaPorId(@PathVariable Long id) {
        return ventaService.buscarVentaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}