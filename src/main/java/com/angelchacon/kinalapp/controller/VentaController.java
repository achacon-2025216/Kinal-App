package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.service.IVentaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    // LISTAR
    @GetMapping
    public List<Venta> listar() {
        return ventaService.listarTodos();
    }

    // BUSCAR
    @GetMapping("/{codigo}")
    public ResponseEntity<Venta> buscar(@PathVariable Integer codigo) {
        return ventaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta) {
        try {
            return ResponseEntity.ok(ventaService.guardar(venta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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