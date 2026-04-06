package com.sena.tienda.controller;

import com.sena.tienda.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/jerarquico")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Map<String, Integer>>> obtenerJerarquia() {
        // Retorna JSON como: { "MTB": { "GW": 5, "Trek": 10 }, "RUTA": { "Sunday": 20 } }
        return ResponseEntity.ok(inventarioService.obtenerInventarioJerarquico());
    }
}