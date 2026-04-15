package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
*Anotacion que registra un Bean como Bean de Spring
* que la clase contiene la logica de negocio
**/
@Service
/*
* Por defecto todos los metodos de esta clase seran
* transaccionales una transaccion es que puede o no ocurrir
* algo
* */
@Transactional
public class ClienteService implements IClienteService {
    /*
    * private: solo es accesible dentro de la misma clase
    * final: no puede cambiar porque es constante
    * ClienteReposiotory: es el repositorio para acceder para
    * la base de datos eso se llama inyeccion de dependicas ya que
    * Spring nos da el repositorio
    * */
    private final ClienteRepository clienteRepository;
    /*
     * Constructor: este se ejecuta a la hora de crear un
     * objeto Spring pasa el repositorio autimaticamente
     * se conoce como (inyecccion de dependicas)
     * */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        //asignar el repositorio a nuestra varibale de la clase
    }

    //indica que se esta implementando un metodo de la interfaz
    @Override
    //optimizar la consulta, solo lectura para que no bloque la base de datos
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        //findALL es un metodo de spring que hace el select * from Cliente
        //este metodo es de JPARepository
    }

    //Actico 1
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado(1);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
        * Metodo de guardar, crea un cleinte
        * aca es done colocamos la logica del negocio antes de guardas
        * Primero valimados el dato
        * */
        validarCliente(cliente);
        if (cliente.getEstado() == null || cliente.getEstado() == 0) {
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional (readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Buscar un cliente por dpi
        return clienteRepository.findById(dpi);
        //Opcional nos evita el NullPointerException
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Metodo para actualizar un cliente existente
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro con el DPI"+dpi);
            //si no existe se lanza una exception(error controlado)
        }
        cliente.setDpiCliente(dpi);
        //Aseguramps que el DPI del obejeto conincida con el de la RUL
        //Por seguridad usamos el DPI de la URL y no el que viene en el JSON
        validarCliente(cliente);
        return clienteRepository.save(cliente);
        /*
        * save() este no solo sirve para guardar sino tmabien para actualizar Si el dato
        * Existe (dpi) entonces hace UDPATE pero si no existe hace un INSERT pero
        * ante vereficamos si existe o no el registro
        * */
    }

    @Override
    public void eliminar(String dpi) {
        //Elimina un cliente
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro con el DPI"+dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        //vereficar si existe un cliente
        return clienteRepository.existsById(dpi);
    }

    //metodo privado(solo puede utilizacse dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
        *Validaciones del negocio: este metodo hara privado porque
        * es algo interno del servicio
        **/
        if (cliente.getDpiCliente() == null || cliente.getDpiCliente().trim().isEmpty()){
            //Si el dpi es null o esta vacio despues de quitar espaicos
            //lanza una excepcion con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }
        if (cliente.getNombreCliente() ==null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }
    }

}
