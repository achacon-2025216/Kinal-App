package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        // Si el modelo ya trae un cliente (por ejemplo de un error de validación), no lo sobrescribimos
        if (!model.containsAttribute("clienteEditando")) {
            model.addAttribute("clienteEditando", new Cliente());
        }
        model.addAttribute("listaClientes", clienteService.listarTodos());
        return "html/listarCliente";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "termino", required = false) String termino, Model model) {
        if (termino == null || termino.trim().isEmpty()) {
            return "redirect:/clientes";
        }

        List<Cliente> resultados = clienteService.buscarClientes(termino.trim());
        model.addAttribute("listaClientes", resultados);
        model.addAttribute("clienteEditando", new Cliente());
        return "html/listarCliente";
    }

    @GetMapping("/editar/{dpi}")
    public String formularioEditar(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        model.addAttribute("clienteEditando", cliente);
        model.addAttribute("listaClientes", clienteService.listarTodos());
        return "html/listarCliente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("clienteEditando") Cliente cliente, RedirectAttributes flash) {
        try {
            clienteService.guardar(cliente);
            flash.addFlashAttribute("success", "Cliente procesado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi, RedirectAttributes flash) {
        try {
            clienteService.eliminar(dpi);
            flash.addFlashAttribute("success", "Cliente eliminado.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el cliente.");
        }
        return "redirect:/clientes";
    }
}