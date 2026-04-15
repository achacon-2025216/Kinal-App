package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.service.IDetalleVentaService;

import com.angelchacon.kinalapp.service.IProductoService;
import com.angelchacon.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detalles")
public class DetalleVentaController {

    private final IDetalleVentaService detalleService;
    private final IVentaService ventaService;
    private final IProductoService productoService;

    public DetalleVentaController(IDetalleVentaService detalleService, IVentaService ventaService, IProductoService productoService) {
        this.detalleService = detalleService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    // Listar todo al cargar la página
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaDetalles", detalleService.listarTodos());
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("detalleNuevo", new DetalleVenta());
        return "html/listarDetalleVenta";
    }

    // Guardar o Actualizar (Cálculo de subtotal incluido)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("detalleNuevo") DetalleVenta detalle) {
        if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
            java.math.BigDecimal cantidadComoDecimal = new java.math.BigDecimal(detalle.getCantidad());
            java.math.BigDecimal resultado = detalle.getPrecioUnitario().multiply(cantidadComoDecimal);
            detalle.setSubtotal(resultado);
        }

        detalleService.guardar(detalle);
        return "redirect:/detalles";
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        // CAMBIO AQUÍ: Agregamos .orElse(null) para que no dé error de tipos
        DetalleVenta detalle = detalleService.buscarPorId(id).orElse(null);

        model.addAttribute("detalleNuevo", detalle);

        // Recargas las listas para que la página no se rompa
        model.addAttribute("listaDetalles", detalleService.listarTodos());
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarTodos());

        return "html/listarDetalleVenta";
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        detalleService.eliminar(id);
        return "redirect:/detalles";
    }
}