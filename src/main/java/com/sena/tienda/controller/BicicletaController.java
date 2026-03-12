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
@CrossOrigin(origins = "http://localhost:4200")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @GetMapping
    public ResponseEntity<List<Bicicleta>> listar(){
        return ResponseEntity.ok(bicicletaService.listarBicicletas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Bicicleta>> buscar(@RequestParam String texto){

        List<Bicicleta> resultado = bicicletaService.buscar(texto);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/stock-tipo")
    public ResponseEntity<?> stockPorTipo(){
        return ResponseEntity.ok(bicicletaService.stockPorTipo());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo){

        Optional<Bicicleta> bicicleta = bicicletaService.buscarPorCodigo(codigo);

        if(bicicleta.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bicicleta no encontrada");
        }

        return ResponseEntity.ok(bicicleta.get());
    }

    @PostMapping
    public ResponseEntity<?> registrar(
            @Valid @RequestBody Bicicleta bicicleta,
            @RequestParam(defaultValue = "0") int stock){

        try {

            Bicicleta nueva = bicicletaService.registrarBicicleta(bicicleta, stock);

            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (Exception e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{codigo}/stock")
    public ResponseEntity<?> stock(@PathVariable String codigo){

        try{

            int stock = bicicletaService.consultarStock(codigo);

            return ResponseEntity.ok(stock);

        }catch(Exception e){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/stock-total")
    public ResponseEntity<Integer> stockTotal(){
        return ResponseEntity.ok(bicicletaService.stockTotal());
    }
}