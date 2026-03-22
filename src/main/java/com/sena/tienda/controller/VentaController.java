package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.BicicletaService;
import com.sena.tienda.service.ClienteService;
import com.sena.tienda.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodasLasVentas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> ventasPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ventaService.buscarVentasPorCliente(clienteId));
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