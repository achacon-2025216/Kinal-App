package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.service.IClienteService;
import com.angelchacon.kinalapp.service.IUsuarioService;
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
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(new Cliente()); // Inicializa el cliente para evitar el null
        nuevaVenta.setUsuario(new Usuario()); // Inicializa el usuario

        model.addAttribute("ventaEditando", nuevaVenta);
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        return "html/listarVenta";
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