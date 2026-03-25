package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.dto.response.VentaDTO;
import com.sena.tienda.model.Venta;
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
        Venta v;
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            v = ventaService.registrarVentaMultiple(request.getClienteId(), request.getItems());
        } else {
            v = ventaService.registrarVenta(request.getClienteId(), request.getCodigoBicicleta(), request.getCantidad());
        }
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
