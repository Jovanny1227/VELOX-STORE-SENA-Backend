package com.sena.tienda.service;

import com.sena.tienda.model.Proveedor;
import com.sena.tienda.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
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

    public void eliminar(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        proveedorRepository.deleteById(id);
    }
}
