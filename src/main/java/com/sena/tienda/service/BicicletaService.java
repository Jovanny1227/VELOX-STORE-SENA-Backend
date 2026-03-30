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

        Bicicleta bicicleta = new Bicicleta(request.modelo(), request.marca(), request.precio(), request.tipo(), proveedor);
        Bicicleta guardada = bicicletaRepository.save(bicicleta);
        guardada.setCodigo(generarCodigo(guardada.getIdBicicleta()));
        bicicletaRepository.save(guardada);

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
            BicicletaRequest reqIndividual = new BicicletaRequest(
                    item.modelo(), item.marca(), item.precio(), item.tipo(), item.proveedorId()
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

    public List<Bicicleta> listarBicicletas() { return bicicletaRepository.findAll(); }
    public Optional<Bicicleta> buscarPorCodigo(String codigo) { return bicicletaRepository.findByCodigo(codigo); }
    public int stockTotal() { return inventarioRepository.stockTotal() != null ? inventarioRepository.stockTotal() : 0; }
}