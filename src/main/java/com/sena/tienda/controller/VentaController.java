package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.model.Venta;
import com.sena.tienda.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/registrar")
    public Venta registrarVenta(@RequestBody VentaRequest request) {
        return ventaService.registrarVenta(
                request.getClienteId(),
                request.getCodigoBicicleta(),
                request.getCantidad()
        );
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
    public Venta buscarVenta(@PathVariable Long idVenta) {
        return ventaService.buscarVentaPorId(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }
}
