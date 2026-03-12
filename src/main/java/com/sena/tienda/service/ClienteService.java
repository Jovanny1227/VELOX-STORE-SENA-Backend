package com.sena.tienda.service;

import com.sena.tienda.model.Cliente;
import com.sena.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * SERVICIO: ClienteService
 * La PK ahora es Long (cliente_id autoincremental).
 * El documento sigue siendo único pero ya no es la PK.
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Registra un nuevo cliente.
     * Valida que el documento (cédula/NIT) no esté duplicado.
     */
    public Cliente registrarCliente(Cliente cliente) {

        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new RuntimeException(
                    "ERROR: Ya existe un cliente con el documento '" + cliente.getDocumento() + "'.");
        }

        Cliente guardado = clienteRepository.save(cliente);
        System.out.println("✅ Cliente registrado: " + guardado.getNombre()
                + " (cliente_id=" + guardado.getClienteId() + ")");
        return guardado;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    /** Busca por PK (cliente_id) */
    public Optional<Cliente> buscarPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    /** Busca por documento (cédula/NIT) */
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }
}