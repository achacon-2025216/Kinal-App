package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Cliente;
import com.angelchacon.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Cambiado para manejar vistas HTML
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // 1. LISTAR: Ahora devuelve el HTML "listar.html"
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("listaClientes", clienteService.listarTodos());
        model.addAttribute("cliente", new Cliente()); // Objeto para la modal (crear/editar)
        return "html/listarCliente";
    }

    // 2. FORMULARIO NUEVO: Crea un objeto vacío y abre la página
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("esEdicion", false);
        return "html/listarCliente";
    }

    // 3. EDITAR: Busca el cliente por DPI y lo manda al formulario
    @GetMapping("/editar/{dpi}")
    public String formularioEditar(@PathVariable String dpi, Model model) {
        // 1. Buscamos al cliente para editar
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. IMPORTANTE: Volvemos a traer la lista para que la tabla NO se vacíe
        model.addAttribute("listaClientes", clienteService.listarTodos());

        // 3. Pasamos el cliente encontrado al objeto que usa el formulario
        model.addAttribute("cliente", cliente);
        model.addAttribute("esEdicion", true);

        return "html/listarCliente";
    }

    // 4. GUARDAR: Recibe los datos y hace un "redirect" a la tabla
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    // 5. ELIMINAR: Borra el registro y refresca la lista
    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        clienteService.eliminar(dpi);
        return "redirect:/clientes";
    }
}