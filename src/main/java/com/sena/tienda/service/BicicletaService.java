package com.sena.tienda.service;

import com.sena.tienda.dto.request.BicicletaRequest;
import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.model.Inventario;
import com.sena.tienda.model.Proveedor;
import com.sena.tienda.model.DetalleVenta; // <- Importa esto
import com.sena.tienda.repository.BicicletaRepository;
import com.sena.tienda.repository.InventarioRepository;
import com.sena.tienda.repository.MovimientoInventarioRepository;
import com.sena.tienda.repository.ProveedorRepository;
import com.sena.tienda.repository.DetalleVentaRepository; // <- Importa esto
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
    private final DetalleVentaRepository detalleVentaRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository,
                            InventarioRepository inventarioRepository,
                            MovimientoInventarioRepository movimientoRepository,
                            ProveedorRepository proveedorRepository,
                            DetalleVentaRepository detalleVentaRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoRepository = movimientoRepository;
        this.proveedorRepository = proveedorRepository;
        this.detalleVentaRepository = detalleVentaRepository;
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

        // --- SOLUCIÓN: Agregar esto para registrar el historial del movimiento ---
        if (stockInicial > 0) {
            // Importa TipoMovimiento (import com.sena.tienda.model.TipoMovimiento;) y MovimientoInventario si no están
            com.sena.tienda.model.MovimientoInventario movimiento = new com.sena.tienda.model.MovimientoInventario(
                    guardada,
                    proveedor,
                    com.sena.tienda.model.TipoMovimiento.ENTRADA,
                    stockInicial,
                    request.precio(),
                    "Inventario inicial al registrar la bicicleta"
            );
            movimientoRepository.save(movimiento);
        }
        // ------------------------------------------------------------------------

        return guardada;
    }

    @Transactional
    public void eliminarBicicleta(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        // 1. Borrar movimientos de inventario (El código que ya teníamos)
        List<com.sena.tienda.model.MovimientoInventario> movimientos = movimientoRepository.findByCodigoBicicleta(bicicleta.getCodigo());
        if (!movimientos.isEmpty()) {
            movimientoRepository.deleteAll(movimientos);
        }

        // 2. NUEVO PASO: Borrar Detalles de Venta asociados a la bicicleta
        List<DetalleVenta> detalles = detalleVentaRepository.findByBicicleta(bicicleta);
        if (!detalles.isEmpty()) {
            detalleVentaRepository.deleteAll(detalles);
        }

        // 3. Borrar el inventario total
        inventarioRepository.findByBicicletaIdBicicleta(id).ifPresent(inventarioRepository::delete);

        // 4. Finalmente, borrar la bicicleta
        bicicletaRepository.delete(bicicleta);
    }

    public List<Bicicleta> listarBicicletas() { return bicicletaRepository.findAll(); }
    public Optional<Bicicleta> buscarPorCodigo(String codigo) { return bicicletaRepository.findByCodigo(codigo); }
    public int stockTotal() {
        Integer total = inventarioRepository.stockTotal();
        return total != null ? total : 0;
    }
}