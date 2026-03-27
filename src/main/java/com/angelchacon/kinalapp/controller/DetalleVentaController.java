package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.service.IDetalleVentaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-venta")
public class DetalleVentaController {

    private final IDetalleVentaService service;

    public DetalleVentaController(IDetalleVentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DetalleVenta> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public DetalleVenta guardar(@RequestBody DetalleVenta detalle) {
        return service.guardar(detalle);
    }

    @PutMapping("/{id}")
    public DetalleVenta actualizar(@PathVariable Integer id, @RequestBody DetalleVenta detalle) {
        detalle.setCodigoDetalleVenta(id);
        return service.guardar(detalle);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}