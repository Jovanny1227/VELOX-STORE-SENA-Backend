package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.dto.response.VentaDTO;
import com.sena.tienda.service.VentaService;
import org.springframework.http.ResponseEntity;
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
    public VentaDTO registrarVenta(@RequestBody VentaRequest request) {
        var v = ventaService.registrarVenta(request.getClienteId(), request.getCodigoBicicleta(), request.getCantidad());
        // Como usamos un "record", los getters se llaman sin la palabra "get" (ej. v.total() en lugar de v.getTotal() si fuera un DTO normal, pero aquí sacamos datos de la entidad "v" que sí tiene getters)
        return new VentaDTO(v.getIdVenta(), v.getCliente().getNombre(), v.getFecha(), v.getTotal());
    }

    @GetMapping
    public List<VentaDTO> listarVentas() {
        return ventaService.listarTodasLasVentas().stream()
                .map(v -> new VentaDTO(v.getIdVenta(), v.getCliente().getNombre(), v.getFecha(), v.getTotal()))
                .toList();
    }

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long idVenta) {
        ventaService.eliminarVenta(idVenta);
        return ResponseEntity.noContent().build();
    }
}