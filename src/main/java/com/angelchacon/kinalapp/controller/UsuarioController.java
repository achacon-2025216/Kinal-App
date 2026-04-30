package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.service.IUsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(IUsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // --- RUTAS DE GESTIÓN (Mantenlas si las usas) ---

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        model.addAttribute("usuarioEditando", new Usuario());
        return "html/listarUsuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute("usuarioEditando") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    // --- RUTAS DE AUTENTICACIÓN ---

    // 1. ELIMINA mostrarLogin() -> Ya lo hace el IndexController
    // 2. ELIMINA login(...) -> Spring Security lo hace internamente
    // 3. ELIMINA logout(...) -> Spring Security lo hace internamente

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioNuevo", new Usuario());
        return "html/registro";
    }

    
    @PostMapping("/registro/guardar")
    public String registrar(@ModelAttribute("usuario") Usuario usuario) {
        // 1. Forzamos el prefijo ROLE_ para que Spring Security lo reconozca
        if (!usuario.getRol().startsWith("ROLE_")) {
            usuario.setRol("ROLE_" + usuario.getRol());
        }

        // 2. Guardamos (Asegúrate de que tu servicio use el repositorio de MySQL)
        usuarioService.guardar(usuario);

        return "redirect:/login?success";
    }
}