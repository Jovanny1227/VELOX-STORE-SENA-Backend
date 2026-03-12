package com.sena.tienda.repository;

import com.sena.tienda.model.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {

    Optional<Bicicleta> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Query("""
    SELECT b FROM Bicicleta b
    WHERE LOWER(b.codigo) LIKE LOWER(CONCAT('%', :texto, '%'))
    OR LOWER(b.marca) LIKE LOWER(CONCAT('%', :texto, '%'))
    OR LOWER(b.modelo) LIKE LOWER(CONCAT('%', :texto, '%'))
    OR LOWER(b.tipo) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Bicicleta> buscar(@Param("texto") String texto);
}