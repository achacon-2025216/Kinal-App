package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.repository.ClienteRepository;
import com.angelchacon.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controler = @Resquestbody
@RequestMapping("/clientes")
//Todas las tutas en este controlador deben empezar por /clientes

public class ClienteController {
    //Inyectos el servicio de no el repositorio
    //El controlador solo debe de tener conexion con el Servico
    private final IClienteService clienteService;
    //Como buena practica la Inyeccion de dependicas debe hacer por el contructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //Responde peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Cliente>> listar (){
        List<Cliente> clientes = clienteService.listarTodos();
        //delegamos al servicio
        // 200 ok con la lista de clientes
        return ResponseEntity.ok(clientes);
    }

    //Responde peticiones del activo
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        List<Cliente> activos = clienteService.listarActivos();
        return ResponseEntity.ok(activos);
    }

    //{dpi} es una variabale de ruta (valor o buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI (@PathVariable String dpi){
        //@PathVaribale toma el valor de la URl y lo asigna al dpi
            return clienteService.buscarPorDPI(dpi)
                   //Si optional tiene valor, devuleve 200 ok con el cliente
                    .map(ResponseEntity::ok)
                    //Si optional esta vacio, devuelve 404 not found
                    .orElse(ResponseEntity.notFound().build());
    }

    //Post crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar (@RequestBody Cliente cliente){
        //@REsquestBody: toma el JSON del cuerpo y lo convierte a un objeto de tipo cliente
        //<?> significa tipo generico pude ser un cliente o un String
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intenamos guardar el cliente pero lanzar una excpetion
            //de ILegalArgumentException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREADT(mucho msa especifico que el 200 para la creaacion de un cliente)

        }catch (IllegalArgumentException e){
            //si hay error de validacion
            //404 BAD RESQUEST con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE elimna un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void> : NO devuelve cuerpo en la respuetsa
        try {
            if(!clienteService.existePorDPI(dpi)){
                return ResponseEntity.notFound().build();
                //404 Si no existe
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuto correctamente y no devuelve cuerpo)
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //Actualizar cliente a traves del DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try {
            if (!clienteService.existePorDPI(dpi)){
                //Vereficar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            //Actualizamos el cliente pero esto puede lanzar una exception
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
            //200 ok con el cliente ya actualizado

        }catch (IllegalArgumentException e){
            //Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            //Posiblemente cualquier otro error como: cliente no encontrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

}



