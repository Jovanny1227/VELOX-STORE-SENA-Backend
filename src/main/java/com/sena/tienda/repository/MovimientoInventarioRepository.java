package com.sena.tienda.repository;

import com.sena.tienda.model.MovimientoInventario;
import com.sena.tienda.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByBicicletaIdBicicletaOrderByFechaDesc(Long idBicicleta);

    List<MovimientoInventario> findByTipoOrderByFechaDesc(TipoMovimiento tipo);

    @Query("SELECT m FROM MovimientoInventario m ORDER BY m.fecha DESC")
    List<MovimientoInventario> findAllOrderByFechaDesc();

    @Query("SELECT m FROM MovimientoInventario m WHERE m.bicicleta.codigo = :codigo ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByCodigoBicicleta(@Param("codigo") String codigo);
}
