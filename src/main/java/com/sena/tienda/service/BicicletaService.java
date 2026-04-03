package com.sena.tienda.service;

import com.sena.tienda.dto.request.BicicletaRequest;
import com.sena.tienda.dto.request.BicicletaMasivaRequest;
import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProveedorRepository proveedorRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository, InventarioRepository inventarioRepository,
                            MovimientoInventarioRepository movimientoRepository, ProveedorRepository proveedorRepository,
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

    // Registro Individual
    @Transactional
    public Bicicleta registrarBicicleta(BicicletaRequest request, int stockInicial) {
        Proveedor proveedor = proveedorRepository.findById(request.proveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + request.proveedorId()));

        // Creamos la bicicleta
        Bicicleta bicicleta = new Bicicleta(request.modelo(), request.marca(), request.precio(), request.tipo(), proveedor);

        // ¡AQUÍ ESTABA EL ERROR! Debemos pasarle el stock al objeto antes de guardar
        bicicleta.setStock(stockInicial); // <--- ESTA LÍNEA FALTABA

        Bicicleta guardada = bicicletaRepository.save(bicicleta);
        guardada.setCodigo(generarCodigo(guardada.getIdBicicleta()));
        bicicletaRepository.save(guardada);

        // Esto ya lo tenías bien (guarda en la tabla Inventario)
        Inventario inventario = new Inventario();
        inventario.setBicicleta(guardada);
        inventario.setCantidadDisponible(stockInicial);
        inventarioRepository.save(inventario);

        if (stockInicial > 0) {
            MovimientoInventario movimiento = new MovimientoInventario(
                    guardada, proveedor, TipoMovimiento.ENTRADA, stockInicial, request.precio(), "Inventario inicial"
            );
            movimientoRepository.save(movimiento);
        }
        return guardada;
    }

    // Registro Masivo (Requerimiento)
    @Transactional
    public List<Bicicleta> registrarMasivo(BicicletaMasivaRequest request) {
        List<Bicicleta> bicicletasGuardadas = new ArrayList<>();
        for (BicicletaMasivaRequest.ItemBicicleta item : request.items()) {

            // ¡AQUÍ ESTABA EL ERROR! Le faltaba el sexto parámetro (el stock) al final
            BicicletaRequest reqIndividual = new BicicletaRequest(
                    item.modelo(),
                    item.marca(),
                    item.precio(),
                    item.tipo(),
                    item.proveedorId(),
                    item.cantidad() // <--- ESTO ES LO QUE FALTABA
            );

            bicicletasGuardadas.add(registrarBicicleta(reqIndividual, item.cantidad()));
        }
        return bicicletasGuardadas;
    }

    // Catálogo con Filtros (Requerimiento)
    public List<Bicicleta> buscarCatalogo(String marca, TipoBicicleta tipo, BigDecimal precioMax) {
        return bicicletaRepository.findAll().stream()
                .filter(b -> marca == null || b.getMarca().equalsIgnoreCase(marca))
                .filter(b -> tipo == null || b.getTipo() == tipo)
                .filter(b -> precioMax == null || b.getPrecio().compareTo(precioMax) <= 0)
                .toList();
    }

    @Transactional
    public void eliminarBicicleta(Long id) {
        Bicicleta bicicleta = bicicletaRepository.findById(id).orElseThrow(() -> new RuntimeException("No encontrada"));
        List<MovimientoInventario> movimientos = movimientoRepository.findByCodigoBicicleta(bicicleta.getCodigo());
        if (!movimientos.isEmpty()) movimientoRepository.deleteAll(movimientos);

        List<DetalleVenta> detalles = detalleVentaRepository.findByBicicleta(bicicleta);
        if (!detalles.isEmpty()) detalleVentaRepository.deleteAll(detalles);

        inventarioRepository.findByBicicletaIdBicicleta(id).ifPresent(inventarioRepository::delete);
        bicicletaRepository.delete(bicicleta);
    }

    @Transactional
    public Bicicleta actualizarBicicleta(Long id, BicicletaRequest request, int nuevoStock) {
        Bicicleta existente = bicicletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        // 🔥 ESTO FALTABA: Buscar el proveedor y actualizarlo 🔥
        Proveedor proveedor = proveedorRepository.findById(request.proveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + request.proveedorId()));

        existente.setModelo(request.modelo());
        existente.setMarca(request.marca());
        existente.setPrecio(request.precio());
        existente.setTipo(request.tipo());
        existente.setStock(nuevoStock);
        existente.setProveedor(proveedor); // <--- ASIGNAMOS EL PROVEEDOR AQUÍ

        return bicicletaRepository.save(existente);
    }
    public List<Bicicleta> listarBicicletas() { return bicicletaRepository.findAll(); }
    public Optional<Bicicleta> buscarPorCodigo(String codigo) { return bicicletaRepository.findByCodigo(codigo); }
    public int stockTotal() { return inventarioRepository.stockTotal() != null ? inventarioRepository.stockTotal() : 0; }
}