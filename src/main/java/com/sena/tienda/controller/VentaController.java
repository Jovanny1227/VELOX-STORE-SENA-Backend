package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaPresencialRequest;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Ventas", description = "Endpoints para la gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping("/pos")
    @Operation(summary = "Registrar venta POS con múltiples productos y cliente")
    public ResponseEntity<Venta> registrarVentaMultiple(@RequestParam Long usuarioId, @RequestBody VentaPresencialRequest request) {
        return ResponseEntity.ok(ventaService.registrarVentaMultiple(usuarioId, request));
    }
    
    @GetMapping
    @Operation(summary = "Obtener todas las ventas")
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }
}
