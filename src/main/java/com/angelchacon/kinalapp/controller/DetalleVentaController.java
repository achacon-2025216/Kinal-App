package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.service.IDetalleVentaService;
import com.angelchacon.kinalapp.service.IProductoService;
import com.angelchacon.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
        // Esta línea ahora sí traerá datos porque listarTodos() ya no devuelve una lista vacía
        model.addAttribute("listaDetalles", detalleService.listarTodos());
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("detalleNuevo", new DetalleVenta());

        return "html/listarDetalleVenta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("detalleNuevo") DetalleVenta detalle) {
        // Buscamos el producto para obtener su precio unitario real
        Producto prod = productoService.buscarPorCodigo(detalle.getProducto().getCodigoProducto()).orElse(null);

        if (prod != null && detalle.getCantidad() != null) {
            // Seteamos el precio desde el catálogo de productos
            detalle.setPrecioUnitario(prod.getPrecio());

            // Calculamos subtotal: Precio * Cantidad
            BigDecimal subtotal = prod.getPrecio().multiply(new BigDecimal(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);

            detalleService.guardar(detalle);
        }

        return "redirect:/detalles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        detalleService.eliminar(id);
        return "redirect:/detalles";
    }

    // El método editar se mantiene igual, pero recuerda que al editar
    // tendrías que manejar la lógica de ajuste de stock (sumar el viejo y restar el nuevo)
    @GetMapping("/buscar")
    public String buscar(@RequestParam("termino") String termino, Model model) {
        // Aquí puedes filtrar por ID de factura o nombre de producto
        model.addAttribute("listaDetalles", detalleService.listarTodos());
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaProductos", productoService.listarTodos());
        model.addAttribute("detalleNuevo", new DetalleVenta());
        return "html/listarDetalleVenta";
    }
}