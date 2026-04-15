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

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaDetalles", detalleService.listarTodos());
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("detalleNuevo", new DetalleVenta());
        return "html/listarDetalleVenta"; // Nombre del archivo HTML
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("detalleNuevo") DetalleVenta detalle) {
        // 1. Verificamos que cantidad y precio no sean nulos para evitar errores
        if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {

            // 2. Calculamos: Subtotal = Precio * Cantidad
            // Usamos BigDecimal para que el cálculo sea exacto con los decimales
            java.math.BigDecimal cantidadComoDecimal = new java.math.BigDecimal(detalle.getCantidad());
            java.math.BigDecimal resultado = detalle.getPrecioUnitario().multiply(cantidadComoDecimal);

            // 3. Le asignamos el resultado al objeto antes de mandarlo a la DB
            detalle.setSubtotal(resultado);
        }

        detalleService.guardar(detalle);
        return "redirect:/detalles";
    }


    @PutMapping("/{id}")
    public DetalleVenta actualizar(@PathVariable Integer id, @RequestBody DetalleVenta detalle) {
        detalle.setCodigoDetalleVenta(id);
        return detalleService.guardar(detalle);
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        detalleService.eliminar(id);
        return "redirect:/detalles";
    }
}