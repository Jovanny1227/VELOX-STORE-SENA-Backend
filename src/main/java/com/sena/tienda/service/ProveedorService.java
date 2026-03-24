package com.sena.tienda.service;

import com.sena.tienda.model.Proveedor;
import com.sena.tienda.model.Bicicleta; // <- Importar
import com.sena.tienda.repository.ProveedorRepository;
import com.sena.tienda.repository.BicicletaRepository; // <- Importar
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final BicicletaRepository bicicletaRepository; // <- Agregar
    private final BicicletaService bicicletaService;

    public ProveedorService(ProveedorRepository proveedorRepository,
                            BicicletaRepository bicicletaRepository,
                            BicicletaService bicicletaService) {
        this.proveedorRepository = proveedorRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.bicicletaService = bicicletaService;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor registrar(Proveedor proveedor) {
        if (proveedor.getNit() != null && proveedorRepository.existsByNit(proveedor.getNit())) {
            throw new RuntimeException("Ya existe un proveedor con el NIT: " + proveedor.getNit());
        }
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(Long id, Proveedor datos) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        existente.setNombre(datos.getNombre());
        existente.setNit(datos.getNit());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        return proveedorRepository.save(existente);
    }

    @Transactional
    public void eliminarProveedor(Long idProveedor) {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + idProveedor));

        // 1. Buscar si hay bicicletas asociadas a este proveedor
        List<Bicicleta> bicicletasAsociadas = bicicletaRepository.findByProveedor(proveedor);

        // 2. Si hay bicicletas, primero eliminamos las bicicletas (y su historial)
        if (!bicicletasAsociadas.isEmpty()) {
            for (Bicicleta bici : bicicletasAsociadas) {
                // Reutilizamos el método que ya limpia inventario, ventas y movimientos
                bicicletaService.eliminarBicicleta(bici.getIdBicicleta());
            }
        }

        // 3. Finalmente, borrar el proveedor
        proveedorRepository.delete(proveedor);
    }
}
