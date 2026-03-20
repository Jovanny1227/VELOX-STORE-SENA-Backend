package com.sena.tienda.service;

import com.sena.tienda.model.Cliente;
import com.sena.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente registrarCliente(Cliente cliente) {
        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con el documento: " + cliente.getDocumento());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizarCliente(Long id, Cliente datos) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        existente.setNombre(datos.getNombre());
        existente.setTelefono(datos.getTelefono());
        if (!existente.getDocumento().equals(datos.getDocumento()) &&
            clienteRepository.existsByDocumento(datos.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con el documento: " + datos.getDocumento());
        }
        existente.setDocumento(datos.getDocumento());
        return clienteRepository.save(existente);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado: " + id);
        }
        clienteRepository.deleteById(id);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteRepository.findByDocumento(documento);
    }
}