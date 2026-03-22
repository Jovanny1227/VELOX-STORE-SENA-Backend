package com.sena.tienda.service;

import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.model.Inventario;
import com.sena.tienda.repository.BicicletaRepository;
import com.sena.tienda.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository,
                            InventarioRepository inventarioRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
    }

    private String generarCodigo(Long id) {
        return String.format("BIC-%03d", id);
    }

    @Transactional
    public Bicicleta registrarBicicleta(Bicicleta bicicleta, int stockInicial) {
        if (stockInicial < 0) throw new RuntimeException("El stock no puede ser negativo");
        Bicicleta guardada = bicicletaRepository.save(bicicleta);
        guardada.setCodigo(generarCodigo(guardada.getIdBicicleta()));
        bicicletaRepository.save(guardada);
        Inventario inventario = new Inventario();
        inventario.setBicicleta(guardada);
        inventario.setCantidadDisponible(stockInicial);
        inventarioRepository.save(inventario);
        return guardada;
    }

    @Transactional
    public void eliminarBicicleta(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + id));
        inventarioRepository.findByBicicletaIdBicicleta(id).ifPresent(inventarioRepository::delete);
        bicicletaRepository.delete(bicicleta);
    }

    public List<Bicicleta> listarBicicletas() {
        return bicicletaRepository.findAll();
    }

    public Optional<Bicicleta> buscarPorCodigo(String codigo) {
        return bicicletaRepository.findByCodigo(codigo);
    }

    public List<Bicicleta> buscar(String texto) {
        return bicicletaRepository.buscar(texto);
    }

    public int consultarStock(String codigoBicicleta) {
        Bicicleta b = bicicletaRepository.findByCodigo(codigoBicicleta)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));
        return inventarioRepository.findByBicicletaIdBicicleta(b.getIdBicicleta())
                .map(Inventario::getCantidadDisponible).orElse(0);
    }

    public int stockTotal() {
        Integer total = inventarioRepository.stockTotal();
        return total != null ? total : 0;
    }

    public Map<String, Integer> stockPorTipo() {
        Map<String, Integer> mapa = new HashMap<>();
        for (Object[] r : inventarioRepository.stockPorTipo()) {
            mapa.put(r[0].toString(), ((Number) r[1]).intValue());
        }
        return mapa;
    }
}