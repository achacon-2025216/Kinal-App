package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    /*
    * Interfaz: Es un contrato que dice QUE metodos debe tener
    * cualquier servicio de Clientes, No tiene
    * Implementacion, solo la defincion de los metodos
    **/

    //Metodo que devuelve un alista de todos los cliemtes
    List<Cliente>listarTodos();
    // Lista solo los clientes activos (estado = 1)
    List<Cliente> listarActivos();
    /*
    *List<Cliente> lo que hace es devolver una lista
    * de objetos de la entidad clientes
    **/

    //Metodo que guarda un cliente en la base de datos
    Cliente guardar (Cliente cliente);
    //Parametos: recibe un objeto Cliente con los datos a guardar

    //Optional * Contenedot que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Cliente>buscarPorDPI(String dpi);

    //Metodo que actuazliza un cliente
    Cliente actualizar (String dpi, Cliente cliente);
    /*
    * Parametros -dpi> DPI del cliente a actualizar
    * CLiente cliente con los datos nuevos
    * Retorna un objeto tipo Cliente ya actualizado
    **/

    /*
    * Metodo de tipo void para eliminar a un cliente
    * void: no retorna ningun valor ninguna dato
    * Elimina un Cliente por su DPi
    **/
    void eliminar (String dpi);

    //bollean -Retornar true si existe y false si no existe
    boolean existePorDPI (String dpi);

}
