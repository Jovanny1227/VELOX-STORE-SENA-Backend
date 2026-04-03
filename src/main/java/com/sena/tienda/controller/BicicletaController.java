package com.sena.tienda.controller;

import com.sena.tienda.dto.request.BicicletaRequest;
import com.sena.tienda.dto.request.BicicletaMasivaRequest;
import com.sena.tienda.dto.response.BicicletaDTO;
import com.sena.tienda.model.TipoBicicleta;
import com.sena.tienda.service.BicicletaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
public class BicicletaController {

    private final BicicletaService bicicletaService;

    public BicicletaController(BicicletaService bicicletaService) {
        this.bicicletaService = bicicletaService;
    }

    // CATÁLOGO PÚBLICO CON FILTROS (Solo lectura)
    @GetMapping("/catalogo")
    public ResponseEntity<List<BicicletaDTO>> verCatalogo(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) TipoBicicleta tipo,
            @RequestParam(required = false) BigDecimal precioMax) {

        List<BicicletaDTO> catalogo = bicicletaService.buscarCatalogo(marca, tipo, precioMax).stream()
                .map(b -> new BicicletaDTO(b.getIdBicicleta(), b.getCodigo(), b.getModelo(), b.getMarca(),
                        b.getPrecio(), b.getTipo(), b.getProveedor() != null ? b.getProveedor().getNombre() : "Sin Proveedor", b.getStock()))
                .toList();
        return ResponseEntity.ok(catalogo);
    }

    // CARGA MASIVA (Solo Admin)
    @PostMapping("/masivo")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registrarMasivo(@Valid @RequestBody BicicletaMasivaRequest request) {
        bicicletaService.registrarMasivo(request);
        return ResponseEntity.ok("Bicicletas registradas masivamente con éxito.");
    }

    // CRUD BÁSICO (Solo Admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BicicletaDTO> listar() {
        return bicicletaService.listarBicicletas().stream()
                .map(b -> new BicicletaDTO(b.getIdBicicleta(), b.getCodigo(), b.getModelo(), b.getMarca(),
                        b.getPrecio(), b.getTipo(), b.getProveedor() != null ? b.getProveedor().getNombre() : "N/A", b.getStock()))
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BicicletaDTO registrar(@Valid @RequestBody BicicletaRequest request) {
        int stockInicial = request.stock() != null ? request.stock() : 0;
        var b = bicicletaService.registrarBicicleta(request, stockInicial);
        return new BicicletaDTO(b.getIdBicicleta(), b.getCodigo(), b.getModelo(), b.getMarca(), b.getPrecio(), b.getTipo(), b.getProveedor().getNombre(), b.getStock());
    }

    // 🔥 AQUÍ ESTÁ LA SOLUCIÓN: Método PUT para actualizar 🔥
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BicicletaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody BicicletaRequest request) {
        int nuevoStock = request.stock() != null ? request.stock() : 0;

        var b = bicicletaService.actualizarBicicleta(id, request, nuevoStock);

        BicicletaDTO responseDTO = new BicicletaDTO(b.getIdBicicleta(), b.getCodigo(), b.getModelo(), b.getMarca(), b.getPrecio(), b.getTipo(), b.getProveedor().getNombre(), b.getStock());
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        bicicletaService.eliminarBicicleta(id);
    }
}