package com.sena.tienda.controller;

import com.sena.tienda.dto.request.MovimientoRequest;
import com.sena.tienda.model.MovimientoInventario;
import com.sena.tienda.model.TipoMovimiento;
import com.sena.tienda.service.MovimientoInventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoInventario>> listarTodos() {
        return ResponseEntity.ok(movimientoService.listarTodos());
    }

    @GetMapping("/bicicleta/{codigo}")
    public ResponseEntity<List<MovimientoInventario>> listarPorBicicleta(@PathVariable String codigo) {
        return ResponseEntity.ok(movimientoService.listarPorBicicleta(codigo));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoInventario>> listarPorTipo(@PathVariable TipoMovimiento tipo) {
        return ResponseEntity.ok(movimientoService.listarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody MovimientoRequest request) {
        try {
            MovimientoInventario movimiento = movimientoService.registrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
