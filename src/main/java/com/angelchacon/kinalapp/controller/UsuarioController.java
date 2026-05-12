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

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        model.addAttribute("usuarioEditando", new Usuario());
        return "html/listarUsuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute("usuarioEditando") Usuario usuarioFormulario) {

        // 1. DETERMINAR SI ES EDICIÓN O CREACIÓN
        if (usuarioFormulario.getCodigo() != null && usuarioFormulario.getCodigo() > 0) {
            // --- MODO EDICIÓN ---
            Usuario usuarioDB = usuarioService.buscarPorCodigo(usuarioFormulario.getCodigo()).orElse(null);

            if (usuarioDB != null) {
                usuarioDB.setUsername(usuarioFormulario.getUsername());
                usuarioDB.setEmail(usuarioFormulario.getEmail());
                usuarioDB.setEstado(usuarioFormulario.getEstado());

                // Roles
                String rol = usuarioFormulario.getRol();
                if (rol != null && !rol.startsWith("ROLE_")) {
                    rol = "ROLE_" + rol;
                }
                usuarioDB.setRol(rol);

                // Solo encriptar si el usuario escribió algo en el campo de password
                if (usuarioFormulario.getPassword() != null && !usuarioFormulario.getPassword().isEmpty()) {
                    usuarioDB.setPassword(passwordEncoder.encode(usuarioFormulario.getPassword()));
                }

                usuarioService.guardar(usuarioDB);
                return "redirect:/usuarios";
            }
        }

        // --- MODO NUEVO ---
        // Si no entró al IF de arriba, es un registro nuevo.
        // Forzamos el código a null para que MySQL use AUTO_INCREMENT y evitar el error de StaleObject
        usuarioFormulario.setCodigo(null);

        // Encriptar siempre porque es usuario nuevo
        if (usuarioFormulario.getPassword() != null && !usuarioFormulario.getPassword().isEmpty()) {
            usuarioFormulario.setPassword(passwordEncoder.encode(usuarioFormulario.getPassword()));
        }

        // Formatear rol
        String rolNuevo = usuarioFormulario.getRol();
        if (rolNuevo != null && !rolNuevo.startsWith("ROLE_")) {
            usuarioFormulario.setRol("ROLE_" + rolNuevo);
        }

        usuarioService.guardar(usuarioFormulario);
        return "redirect:/usuarios";
    }


    @GetMapping("/usuarios/editar/{codigo}")
    public String editar(@PathVariable("codigo") Integer codigo, Model model) {
        Usuario usuario = usuarioService.buscarPorCodigo(codigo).orElse(null);
        if (usuario != null) {
            model.addAttribute("usuarioEditando", usuario);
            model.addAttribute("listaUsuarios", usuarioService.listarTodos());
            return "html/listarUsuario";
        }
        return "redirect:/usuarios?error=notfound";
    }

    @GetMapping("/usuarios/eliminar/{codigo}")
    public String eliminar(@PathVariable("codigo") Integer codigo) {
        usuarioService.eliminar(codigo);
        return "redirect:/usuarios";
    }
}