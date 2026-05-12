package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientes(String termino) {
        // Implementación del buscador llamando al repositorio
        return clienteRepository.buscarPorNombreODpi(termino);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        if (cliente.getEstado() == null) cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        return clienteRepository.findById(dpi);
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("No se encontró el cliente con DPI: " + dpi);
        }
        cliente.setDpiCliente(dpi);
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("No se encontró el cliente con DPI: " + dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        return clienteRepository.existsById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado(1);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El DPI es obligatorio");
        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }
}