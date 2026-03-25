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
    public ResponseEntity<Venta> buscar(@PathVariable String codigo) {
        return ventaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR
    @PostMapping
    public ResponseEntity<Venta> guardar(@RequestBody Venta venta) {
        return ResponseEntity.ok(ventaService.guardar(venta));
    }

    // ACTUALIZAR
    @PutMapping("/{codigo}")
    public ResponseEntity<Venta> actualizar(@PathVariable String codigo, @RequestBody Venta venta) {
        venta.setCodigoVenta(codigo);
        return ResponseEntity.ok(ventaService.guardar(venta));
    }

    // ELIMINAR
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        ventaService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}