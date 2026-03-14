package com.sena.tienda.controller;

import com.sena.tienda.model.Cliente;
import com.sena.tienda.service.ClienteService;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        Optional<Cliente> cliente = clienteService.buscarPorId(id);

        if(cliente.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con cliente_id=" + id + " no encontrado");
        }

        return ResponseEntity.ok(cliente.get());
    }

    @GetMapping("/documento/{doc}")
    public ResponseEntity<?> buscarPorDocumento(@PathVariable String doc){

        Optional<Cliente> cliente = clienteService.buscarPorDocumento(doc);

        if(cliente.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente con documento '" + doc + "' no encontrado");
        }

        return ResponseEntity.ok(cliente.get());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Cliente cliente){

        try{

            Cliente nuevo = clienteService.registrarCliente(cliente);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);

        }catch(RuntimeException e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
