package com.sena.tienda.controller;

import com.sena.tienda.dto.response.InventarioDTO;
import com.sena.tienda.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/dashboard")
    public Map<String, Integer> dashboard() {
        return inventarioService.dashboardInventario();
    }

    @GetMapping
    public List<InventarioDTO> obtenerInventario() {
        return inventarioService.obtenerInventario();
    }
}
