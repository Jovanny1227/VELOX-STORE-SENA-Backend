package com.sena.tienda.service;

import com.sena.tienda.model.Inventario;
import com.sena.tienda.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    // ESTRUCTURA JERÁRQUICA: Tipo -> Marca -> Cantidad Disponible
    public Map<String, Map<String, Integer>> obtenerInventarioJerarquico() {
        List<Inventario> inventarios = inventarioRepository.findAll();

        return inventarios.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getBicicleta().getTipo().name(),
                        Collectors.groupingBy(
                                inv -> inv.getBicicleta().getMarca(),
                                Collectors.summingInt(Inventario::getCantidadDisponible)
                        )
                ));
    }
}