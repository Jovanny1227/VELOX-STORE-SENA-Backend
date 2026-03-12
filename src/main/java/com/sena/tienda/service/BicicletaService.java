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

    // Genera código tipo BIC-001
    private String generarCodigo(Long id){
        return String.format("BIC-%03d", id);
    }

    @Transactional
    public Bicicleta registrarBicicleta(Bicicleta bicicleta, int stockInicial){

        if(stockInicial < 0){
            throw new RuntimeException("El stock inicial no puede ser negativo");
        }

        // Guardamos primero para obtener ID
        Bicicleta guardada = bicicletaRepository.save(bicicleta);

        // Generamos código automático
        String codigo = generarCodigo(guardada.getIdBicicleta());
        guardada.setCodigo(codigo);

        // Guardamos código
        bicicletaRepository.save(guardada);

        // Creamos inventario
        Inventario inventario = new Inventario();
        inventario.setBicicleta(guardada);
        inventario.setCantidadDisponible(stockInicial);

        inventarioRepository.save(inventario);

        return guardada;
    }

    public List<Bicicleta> listarBicicletas(){
        return bicicletaRepository.findAll();
    }

    public Optional<Bicicleta> buscarPorCodigo(String codigo){
        return bicicletaRepository.findByCodigo(codigo);
    }

    public List<Bicicleta> buscar(String texto){
        return bicicletaRepository.buscar(texto);
    }

    public int consultarStock(String codigoBicicleta){

        Bicicleta b = bicicletaRepository.findByCodigo(codigoBicicleta)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        return inventarioRepository
                .findByBicicletaIdBicicleta(b.getIdBicicleta())
                .map(Inventario::getCantidadDisponible)
                .orElse(0);
    }

    public int stockTotal(){
        Integer total = inventarioRepository.stockTotal();
        return total != null ? total : 0;
    }

    public Map<String,Integer> stockPorTipo(){

        List<Object[]> resultados = inventarioRepository.stockPorTipo();

        Map<String,Integer> mapa = new HashMap<>();

        for(Object[] r : resultados){

            String tipo = r[0].toString();
            Integer cantidad = ((Number) r[1]).intValue();

            mapa.put(tipo, cantidad);
        }

        return mapa;
    }
}