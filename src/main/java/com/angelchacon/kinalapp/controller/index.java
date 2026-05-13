package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.service.UsuarioService; // Asegúrate de tener esta importación
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class index {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("successmessage", "Bienvenido al Sistema Kinal-App");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "html/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuarioNuevo", new Usuario());
        return "html/registro";
    }

    // ESTO ES LO QUE HACE QUE EL BOTÓN "CREAR CUENTA" FUNCIONE
    @PostMapping("/registro/guardar")
    public String guardarUsuario(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        // El service ya tiene la lógica para ponerle rol "USER" automáticamente
        usuarioService.guardar(usuario);
        // Al terminar, te manda al login para que entres con tu nueva cuenta
        return "redirect:/login?success";
    }
}