package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarVenta(@RequestBody VentaRequest request) {
        try {
            return ResponseEntity.ok(ventaService.registrarVenta(
                    request.getClienteId(),
                    request.getCodigoBicicleta(),
                    request.getCantidad()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.listarTodasLasVentas();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Venta> ventasPorCliente(@PathVariable Long clienteId) {
        return ventaService.buscarVentasPorCliente(clienteId);
    }

    @GetMapping("/{idVenta}")
    public ResponseEntity<?> buscarVenta(@PathVariable Long idVenta) {
        return ventaService.buscarVentaPorId(idVenta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long idVenta) {
        try {
            ventaService.eliminarVenta(idVenta);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}