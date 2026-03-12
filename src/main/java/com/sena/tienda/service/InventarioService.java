package com.sena.tienda.service;

import com.sena.tienda.dto.InventarioDTO;
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

    public List<InventarioDTO> obtenerInventario(){
        return inventarioRepository.obtenerInventarioCompleto();
    }

    public Map<String, Integer> dashboardInventario(){

        Map<String, Integer> dashboard = new HashMap<>();

        int total = inventarioRepository.stockTotal();
        dashboard.put("stockTotal", total);

        List<Object[]> porTipo = inventarioRepository.stockPorTipo();

        for(Object[] r : porTipo){

            String tipo = r[0].toString();
            Integer cantidad = ((Number) r[1]).intValue();

            dashboard.put(tipo, cantidad);
        }

        return dashboard;
    }
}