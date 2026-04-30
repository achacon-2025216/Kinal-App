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

    // --- LISTAR USUARIOS ---
    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        model.addAttribute("usuarioEditando", new Usuario()); // Objeto vacío para el formulario de creación
        return "html/listarUsuario";
    }

    // --- GUARDAR O ACTUALIZAR ---
    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute("usuarioEditando") Usuario usuario) {
        // 1. Verificamos si es un nuevo usuario o edición para manejar la contraseña
        // Si es nuevo (codigo es null) o si la contraseña fue cambiada, la ciframos
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        // 2. Aseguramos el prefijo de rol para Spring Security
        if (usuario.getRol() != null && !usuario.getRol().startsWith("ROLE_")) {
            usuario.setRol("ROLE_" + usuario.getRol());
        }

        usuarioService.guardar(usuario);
        return "redirect:/usuarios";
    }

    /// --- EDITAR USUARIO ---
    @GetMapping("/usuarios/editar/{codigo}")
    public String editar(@PathVariable("codigo") Integer codigo, Model model) { // Cambiado a Integer
        // Usamos el nombre exacto de tu interfaz: buscarPorCodigo
        // Como devuelve un Optional, usamos .orElse(null)
        Usuario usuario = usuarioService.buscarPorCodigo(codigo).orElse(null);

        if (usuario != null) {
            model.addAttribute("usuarioEditando", usuario);
            model.addAttribute("listaUsuarios", usuarioService.listarTodos());
            return "html/listarUsuario";
        }

        return "redirect:/usuarios?error=notfound";
    }

    // --- ELIMINAR USUARIO ---
    @GetMapping("/usuarios/eliminar/{codigo}")
    public String eliminar(@PathVariable("codigo") Integer codigo) { // Cambiado a Integer
        usuarioService.eliminar(codigo);
        return "redirect:/usuarios";
    }

    // --- REGISTRO PÚBLICO ---
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioNuevo", new Usuario());
        return "html/registro";
    }

    @PostMapping("/registro/guardar")
    public String registrar(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        // Cifrar contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (!usuario.getRol().startsWith("ROLE_")) {
            usuario.setRol("ROLE_" + usuario.getRol());
        }

        usuarioService.guardar(usuario);
        return "redirect:/login?success";
    }
}