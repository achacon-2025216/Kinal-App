package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteService {

    // Devuelve todos los clientes
    List<Cliente> listarTodos();

    // Lista solo los clientes activos
    List<Cliente> listarActivos();

    // NUEVO: Método para buscar (Solo definición)
    List<Cliente> buscarClientes(String termino);

    // Guarda un cliente
    Cliente guardar(Cliente cliente);

    // Busca un cliente por su DPI
    Optional<Cliente> buscarPorDPI(String dpi);

    // Actualiza los datos de un cliente
    Cliente actualizar(String dpi, Cliente cliente);

    // Elimina un cliente por su DPI
    void eliminar(String dpi);

    // Verifica si existe el DPI
    boolean existePorDPI(String dpi);
}