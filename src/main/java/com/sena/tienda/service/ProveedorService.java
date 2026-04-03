package com.sena.tienda.service;

import com.sena.tienda.model.Proveedor;
import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.repository.ProveedorRepository;
import com.sena.tienda.repository.BicicletaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final BicicletaRepository bicicletaRepository;
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
        // ¡Agregamos la dirección que faltaba para que se guarde al editar!
        existente.setDireccion(datos.getDireccion());

        return proveedorRepository.save(existente);
    }

    @Transactional
    public void eliminarProveedor(Long idProveedor) {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + idProveedor));

        // 1. Buscar si hay bicicletas asociadas a este proveedor
        List<Bicicleta> bicicletasAsociadas = bicicletaRepository.findByProveedor(proveedor);

        // 2. ¡DEFENSA ACTIVADA! Si hay bicicletas, detenemos todo y lanzamos un error
        if (!bicicletasAsociadas.isEmpty()) {
            throw new RuntimeException("Protección de Inventario: No se puede eliminar el proveedor porque tiene " + bicicletasAsociadas.size() + " bicicleta(s) asociada(s).");
        }

        // 3. Si la lista está vacía (no hay bicicletas), borramos al proveedor tranquilamente
        proveedorRepository.delete(proveedor);
    }
}