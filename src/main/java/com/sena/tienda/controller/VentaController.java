package com.sena.tienda.controller;

import com.sena.tienda.dto.request.VentaRequest;
import com.sena.tienda.dto.response.BicicletaVentaDTO;
import com.sena.tienda.dto.response.DetalleVentaDTO;
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

        return mapearAVentaDTO(v);
    }

    @GetMapping
    public List<VentaDTO> listarVentas() {
        return ventaService.listarTodasLasVentas().stream()
                .map(this::mapearAVentaDTO)
                .toList();
    }

    @DeleteMapping("/{idVenta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long idVenta) {
        ventaService.eliminarVenta(idVenta);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para no repetir código y mantener limpio el controlador
    private VentaDTO mapearAVentaDTO(Venta v) {
        List<DetalleVentaDTO> detallesDTO = v.getDetalles().stream()
                .map(d -> new DetalleVentaDTO(
                        d.getCantidad(),
                        d.getSubtotal(),
                        new BicicletaVentaDTO(d.getBicicleta().getMarca(), d.getBicicleta().getModelo())
                )).toList();

        return new VentaDTO(v.getIdVenta(), v.getCliente().getNombre(), v.getFecha(), v.getTotal(), detallesDTO);
    }
}