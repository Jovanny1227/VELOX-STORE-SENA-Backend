package com.sena.tienda.service;

import com.sena.tienda.dto.request.MovimientoRequest;
import com.sena.tienda.model.*;
import com.sena.tienda.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final BicicletaRepository bicicletaRepository;
    private final InventarioRepository inventarioRepository;
    private final ProveedorRepository proveedorRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoRepository,
                                        BicicletaRepository bicicletaRepository,
                                        InventarioRepository inventarioRepository,
                                        ProveedorRepository proveedorRepository) {
        this.movimientoRepository = movimientoRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.inventarioRepository = inventarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public MovimientoInventario registrar(MovimientoRequest request) {

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        Bicicleta bicicleta = bicicletaRepository.findByCodigo(request.getCodigoBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada: " + request.getCodigoBicicleta()));

        Inventario inventario = inventarioRepository.findByBicicletaIdBicicleta(bicicleta.getIdBicicleta())
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para: " + request.getCodigoBicicleta()));

        Proveedor proveedor = null;
        if (request.getTipo() == TipoMovimiento.ENTRADA) {
            if (request.getIdProveedor() == null) {
                throw new RuntimeException("Para una ENTRADA se requiere el proveedor");
            }
            proveedor = proveedorRepository.findById(request.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + request.getIdProveedor()));
        }

        int stockActual = inventario.getCantidadDisponible();

        switch (request.getTipo()) {
            case ENTRADA, AJUSTE_POSITIVO, DEVOLUCION ->
                inventario.setCantidadDisponible(stockActual + request.getCantidad());
            case SALIDA_VENTA, AJUSTE_NEGATIVO -> {
                if (stockActual < request.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente. Disponible: " + stockActual);
                }
                inventario.setCantidadDisponible(stockActual - request.getCantidad());
            }
        }

        inventarioRepository.save(inventario);

        MovimientoInventario movimiento = new MovimientoInventario(
                bicicleta, proveedor, request.getTipo(),
                request.getCantidad(), request.getPrecioUnitario(),
                request.getObservacion()
        );

        return movimientoRepository.save(movimiento);
    }

    public List<MovimientoInventario> listarTodos() {
        return movimientoRepository.findAllOrderByFechaDesc();
    }

    public List<MovimientoInventario> listarPorBicicleta(String codigo) {
        return movimientoRepository.findByCodigoBicicleta(codigo);
    }

    public List<MovimientoInventario> listarPorTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipoOrderByFechaDesc(tipo);
    }
}
