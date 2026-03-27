package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.service.IProductoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    // Inyección por constructor (mejor práctica)
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // Obtener todos
    @GetMapping
    public List<Producto> listar() {
        return productoService.listarTodos();
    }

    // Obtener uno por ID
    @GetMapping("/{id}")
    public Optional<Producto> buscar(@PathVariable Integer id) {
        return productoService.buscarPorCodigo(id);
    }

    // Guardar
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    //  Actualizar
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        producto.setCodigoProducto(id);
        return productoService.guardar(producto);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}