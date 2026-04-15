package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.service.IProductoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("productoEditando", new Producto());
        return "html/listarProducto";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.guardar(producto);
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        // Buscamos el producto y lo mandamos al formulario
        model.addAttribute("productoEditando", productoService.buscarPorCodigo(id).orElse(new Producto()));
        // Cargamos la lista para la tabla
        model.addAttribute("listaProductos", productoService.listarTodos());
        return "html/listarProducto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }
}