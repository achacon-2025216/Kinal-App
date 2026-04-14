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
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("listaClientes", clientes); // Esto lo lee el th:each
        return "html/listarCliente";
    }

    // 2. FORMULARIO NUEVO: Crea un objeto vacío y abre la página
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("esEdicion", false);
        return "html/crudCliente";
    }

    // 3. EDITAR: Busca el cliente por DPI y lo manda al formulario
    @GetMapping("/editar/{dpi}")
    public String formularioEditar(@PathVariable String dpi, Model model) {
        Cliente cliente = clienteService.buscarPorDPI(dpi)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("esEdicion", true);
        return "clientes/formulario";
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