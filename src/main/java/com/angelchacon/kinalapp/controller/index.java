package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class index {

    private final IUsuarioService usuarioService;

    public index(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String login(@RequestParam(value = "registrado", required = false) String registrado, Model model) {
        if (registrado != null) {
            model.addAttribute("mensajeExito", "¡Cuenta creada! Ya puedes ingresar.");
        }
        return "html/login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.listarTodos().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogueado", usuario.getUsername());
            session.setAttribute("rol", usuario.getRol().toUpperCase()); // Forzamos Mayúsculas
            return "redirect:/menu";
        } else {
            model.addAttribute("error", "Credenciales incorrectas.");
            return "html/login";
        }
    }

    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return "redirect:/";
        return "index.html"; // Tu archivo de los botones
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioNuevo", new Usuario());
        return "html/registro";
    }

    @PostMapping("/registro/guardar")
    public String guardarUsuario(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        usuario.setEstado(1);
        // Si por alguna razón el email viene vacío, generamos uno para evitar el error Duplicate entry ''
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            usuario.setEmail(usuario.getUsername() + "@kinalapp.com");
        }
        usuarioService.guardar(usuario);
        return "redirect:/?registrado=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}