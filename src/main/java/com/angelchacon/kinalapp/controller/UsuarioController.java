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
    public String registrarNuevo(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        // 1. Encriptar la contraseña (lo que ya hicimos)
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 2. AGREGAR EL PREFIJO ROLE_ (Esto es lo que te falta)
        // Si el usuario eligió "ADMIN", esto lo convierte en "ROLE_ADMIN"
        if (!usuario.getRol().startsWith("ROLE_")) {
            usuario.setRol("ROLE_" + usuario.getRol());
        }

        usuarioService.guardar(usuario);
        return "redirect:/login?success=true";
    }
}