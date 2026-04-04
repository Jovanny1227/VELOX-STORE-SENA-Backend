package com.sena.tienda.repository;

import com.sena.tienda.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * REPOSITORIO: ClienteRepository
 * PK de Cliente ahora es Long (cliente_id - autoincremental)
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar por el campo único documento (cédula/NIT)
    Optional<Cliente> findByDocumento(String documento);

    boolean existsByDocumento(String documento);
}