package com.sena.tienda.controller;

import com.sena.tienda.dto.request.BicicletaRequest;
import com.sena.tienda.dto.response.BicicletaDTO;
import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.service.BicicletaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    @GetMapping
    public List<BicicletaDTO> listar() {
        return bicicletaService.listarBicicletas().stream()
                .map(b -> new BicicletaDTO(
                        b.getIdBicicleta(),
                        b.getCodigo(),
                        b.getModelo(),
                        b.getMarca(),
                        b.getPrecio(),
                        b.getTipo(),
                        b.getProveedor() != null ? b.getProveedor().getNombre() : "Sin Proveedor"
                ))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BicicletaDTO registrar(@Valid @RequestBody BicicletaRequest request, @RequestParam(defaultValue = "0") int stock) {
        Bicicleta b = bicicletaService.registrarBicicleta(request, stock);
        return new BicicletaDTO(
                b.getIdBicicleta(),
                b.getCodigo(),
                b.getModelo(),
                b.getMarca(),
                b.getPrecio(),
                b.getTipo(),
                b.getProveedor().getNombre()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        bicicletaService.eliminarBicicleta(id);
    }

    @GetMapping("/stock-total")
    public int stockTotal() {
        return bicicletaService.stockTotal();
    }
}