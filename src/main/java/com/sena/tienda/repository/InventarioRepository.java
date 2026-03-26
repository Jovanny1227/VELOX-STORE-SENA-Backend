package com.sena.tienda.repository;

import com.sena.tienda.model.Inventario;
import com.sena.tienda.dto.response.InventarioDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByBicicletaIdBicicleta(Long idBicicleta);

    @Query("SELECT SUM(i.cantidadDisponible) FROM Inventario i")
    Integer stockTotal();

    @Query("SELECT b.tipo, SUM(i.cantidadDisponible) FROM Inventario i JOIN i.bicicleta b GROUP BY b.tipo")
    List<Object[]> stockPorTipo();

    @Query("SELECT new com.sena.tienda.dto.response.InventarioDTO(b.codigo, b.marca, b.modelo, b.tipo, b.precio, i.cantidadDisponible) FROM Inventario i JOIN i.bicicleta b")
    List<InventarioDTO> obtenerInventarioCompleto();
}
