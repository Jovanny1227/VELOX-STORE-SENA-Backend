package com.sena.tienda.repository;

import com.sena.tienda.model.Bicicleta;
import com.sena.tienda.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByBicicleta(Bicicleta bicicleta); // <- Agrega esta línea
}