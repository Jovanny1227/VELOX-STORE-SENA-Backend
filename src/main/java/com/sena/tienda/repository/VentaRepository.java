package com.sena.tienda.repository;

import com.sena.tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Cambiamos Cliente por Usuario
    List<Venta> findByUsuarioId(Long usuarioId);
}