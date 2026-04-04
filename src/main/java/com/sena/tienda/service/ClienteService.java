package com.sena.tienda.service;

import com.sena.tienda.model.Cliente;
import com.sena.tienda.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente registrarCliente(Cliente cliente) {
        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con el documento: " + cliente.getDocumento());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));

        if (!existente.getDocumento().equals(datos.getDocumento()) &&
                clienteRepository.existsByDocumento(datos.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con el documento: " + datos.getDocumento());
        }

        existente.setNombre(datos.getNombre());
        existente.setTelefono(datos.getTelefono());
        existente.setDocumento(datos.getDocumento());
        return clienteRepository.save(existente);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado: " + id);
        }
        clienteRepository.deleteById(id);
    }

    public List<Cliente> listarClientes() { return clienteRepository.findAll(); }
    public Optional<Cliente> buscarPorId(Long clienteId) { return clienteRepository.findById(clienteId); }
}