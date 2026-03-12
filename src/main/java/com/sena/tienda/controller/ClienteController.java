package com.sena.tienda.controller;

import com.sena.tienda.model.Cliente;
import com.sena.tienda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLADOR REST: ClienteController
 * BASE URL: http://localhost:8080/api/clientes
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /** GET /api/clientes — Lista todos */
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    /** GET /api/clientes/{id} — Busca por cliente_id (Long) */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> opt = clienteService.buscarPorId(id);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con cliente_id=" + id + " no encontrado.");
        }
        return ResponseEntity.ok(opt.get());
    }

    /** GET /api/clientes/documento/{doc} — Busca por cédula/NIT */
    @GetMapping("/documento/{doc}")
    public ResponseEntity<?> buscarPorDocumento(@PathVariable String doc) {
        Optional<Cliente> opt = clienteService.buscarPorDocumento(doc);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con documento '" + doc + "' no encontrado.");
        }
        return ResponseEntity.ok(opt.get());
    }

    /**
     * POST /api/clientes — Registra un cliente
     *
     * Body JSON:
     * {
     *   "documento": "1234567890",
     *   "nombre": "Juan Pérez",
     *   "telefono": "3001234567"
     * }
     * (cliente_id lo genera MySQL automáticamente)
     */
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(clienteService.registrarCliente(cliente));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}