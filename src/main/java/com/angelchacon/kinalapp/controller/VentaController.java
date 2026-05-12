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

    // LISTAR (Carga inicial)
    @GetMapping
    public String listar(Model model) {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(new Cliente());
        nuevaVenta.setUsuario(new Usuario());

        model.addAttribute("ventaEditando", nuevaVenta);
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        return "html/listarVenta";
    }

    // NUEVO: BUSCAR VENTAS
    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "termino", required = false) String termino, Model model) {
        List<Venta> resultados;

        if (termino != null && !termino.trim().isEmpty()) {
            resultados = ventaService.buscarVentas(termino.trim());
        } else {
            return "redirect:/ventas";
        }

        Venta nuevaVenta = new Venta();
        nuevaVenta.setCliente(new Cliente());
        nuevaVenta.setUsuario(new Usuario());

        model.addAttribute("ventaEditando", nuevaVenta);
        model.addAttribute("listaVentas", resultados);
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        return "html/listarVenta";
    }

    // GUARDAR O ACTUALIZAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("ventaEditando") Venta venta) {
        ventaService.guardar(venta);
        return "redirect:/ventas";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Venta v = ventaService.buscarPorCodigo(id).orElse(new Venta());

        if (v.getCliente() == null) v.setCliente(new Cliente());
        if (v.getUsuario() == null) v.setUsuario(new Usuario());

        model.addAttribute("ventaEditando", v);
        model.addAttribute("listaVentas", ventaService.listarTodos());
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());

        return "html/listarVenta";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return "redirect:/ventas";
    }
}