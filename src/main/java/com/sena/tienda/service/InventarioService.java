package com.sena.tienda.service;

import com.sena.tienda.dto.response.InventarioDTO;
import com.sena.tienda.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<InventarioDTO> obtenerInventario() {
        return inventarioRepository.obtenerInventarioCompleto();
    }

    public Map<String, Integer> dashboardInventario() {
        Map<String, Integer> dashboard = new HashMap<>();
        dashboard.put("stockTotal", inventarioRepository.stockTotal());

        for (Object[] r : inventarioRepository.stockPorTipo()) {
            dashboard.put(r[0].toString(), ((Number) r[1]).intValue());
        }

        return dashboard;
    }
}
