package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        // Esto manda la lista y una venta vacía a la página
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("ventaEditando", new Venta());
        return "html/listarVenta"; // Esto busca el archivo HTML que creaste arriba
    }

    // BUSCAR
    @GetMapping("/{codigo}")
    public ResponseEntity<Venta> buscar(@PathVariable Integer codigo) {
        return ventaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        ventaService.guardar(venta);
        return "redirect:/ventas"; // Recarga la página para ver la nueva venta
    }


    // ACTUALIZAR
    @PutMapping("/{codigo}")
    public ResponseEntity<Venta> actualizar(@PathVariable int codigo, @RequestBody Venta venta) {
        venta.setCodigoVenta(codigo);
        return ResponseEntity.ok(ventaService.guardar(venta));
    }

    // ELIMINAR
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        ventaService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}