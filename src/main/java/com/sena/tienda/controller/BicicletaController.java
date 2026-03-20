package com.sena.tienda.controller;

import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.service.BicicletaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listar() {
        return ResponseEntity.ok(bicicletaService.listarBicicletas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Bicicleta>> buscar(@RequestParam String texto) {
        return ResponseEntity.ok(bicicletaService.buscar(texto));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        Optional<Bicicleta> bicicleta = bicicletaService.buscarPorCodigo(codigo);
        if (bicicleta.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bicicleta no encontrada");
        return ResponseEntity.ok(bicicleta.get());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Bicicleta bicicleta,
                                        @RequestParam(defaultValue = "0") int stock) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bicicletaService.registrarBicicleta(bicicleta, stock));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            bicicletaService.eliminarBicicleta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{codigo}/stock")
    public ResponseEntity<?> stock(@PathVariable String codigo) {
        try {
            return ResponseEntity.ok(bicicletaService.consultarStock(codigo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/stock-total")
    public ResponseEntity<Integer> stockTotal() {
        return ResponseEntity.ok(bicicletaService.stockTotal());
    }
}