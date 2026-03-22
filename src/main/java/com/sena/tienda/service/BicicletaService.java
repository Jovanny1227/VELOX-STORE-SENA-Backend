package com.sena.tienda.service;

import com.sena.tienda.dto.request.BicicletaRequest;
import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.model.Inventario;
import com.sena.tienda.model.Proveedor;
import com.sena.tienda.repository.BicicletaRepository;
import com.sena.tienda.repository.InventarioRepository;
import com.sena.tienda.repository.MovimientoInventarioRepository;
import com.sena.tienda.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProveedorRepository proveedorRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository,
                            InventarioRepository inventarioRepository,
                            MovimientoInventarioRepository movimientoRepository,
                            ProveedorRepository proveedorRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoRepository = movimientoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    private String generarCodigo(Long id) {
        return String.format("BIC-%03d", id);
    }

    @Transactional
    public Bicicleta registrarBicicleta(BicicletaRequest request, int stockInicial) {
        if (stockInicial < 0) throw new RuntimeException("El stock no puede ser negativo");

        // 1. Buscar el proveedor en la BD
        Proveedor proveedor = proveedorRepository.findById(request.proveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + request.proveedorId()));

        // 2. Construir la bicicleta con los datos del Request
        Bicicleta bicicleta = new Bicicleta();
        bicicleta.setModelo(request.modelo());
        bicicleta.setMarca(request.marca());
        bicicleta.setPrecio(request.precio());
        bicicleta.setTipo(request.tipo());
        bicicleta.setProveedor(proveedor);

        // 3. Guardar y generar código
        Bicicleta guardada = bicicletaRepository.save(bicicleta);
        guardada.setCodigo(generarCodigo(guardada.getIdBicicleta()));
        bicicletaRepository.save(guardada);

        // 4. Registrar stock inicial
        Inventario inventario = new Inventario();
        inventario.setBicicleta(guardada);
        inventario.setCantidadDisponible(stockInicial);
        inventarioRepository.save(inventario);

        return guardada;
    }

    @Transactional
    public void eliminarBicicleta(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        if (!movimientoRepository.findByCodigoBicicleta(bicicleta.getCodigo()).isEmpty()) {
            throw new RuntimeException("No se puede eliminar: La bicicleta tiene movimientos de inventario registrados.");
        }

        inventarioRepository.findByBicicletaIdBicicleta(id).ifPresent(inventarioRepository::delete);
        bicicletaRepository.delete(bicicleta);
    }

    public List<Bicicleta> listarBicicletas() { return bicicletaRepository.findAll(); }
    public Optional<Bicicleta> buscarPorCodigo(String codigo) { return bicicletaRepository.findByCodigo(codigo); }
    public int stockTotal() {
        Integer total = inventarioRepository.stockTotal();
        return total != null ? total : 0;
    }
}