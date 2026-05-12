package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("clienteEditando", new Cliente());
        return "html/listarCliente";
    }

    // MÉTODO DE BÚSQUEDA INTEGRADO
    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "termino", required = false) String termino, Model model) {
        List<Cliente> resultados;

        if (termino != null && !termino.trim().isEmpty()) {
            // Buscamos usando el service y limpiamos espacios con trim()
            resultados = clienteService.buscarClientes(termino.trim());
        } else {
            return "redirect:/clientes";
        }

        model.addAttribute("listaClientes", resultados);
        model.addAttribute("clienteEditando", new Cliente());
        return "html/listarCliente";
    }

    @GetMapping("/editar/{dpi}")
    public String formularioEditar(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("clienteEditando", cliente);
        return "html/listarCliente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("clienteEditando") Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        clienteService.eliminar(dpi);
        return "redirect:/clientes";
    }
}