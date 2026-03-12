package com.sena.tienda.repository;

import com.sena.tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * REPOSITORIO: VentaRepository
 * PK es Long (id_venta)
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Listar ventas de un cliente por su cliente_id
    List<Venta> findByClienteClienteId(Long clienteId);
}