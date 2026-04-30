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

        // --- MODO EDICIÓN ---
        if (usuarioFormulario.getCodigo() != null && usuarioFormulario.getCodigo() > 0) {
            Usuario usuarioDB = usuarioService.buscarPorCodigo(usuarioFormulario.getCodigo()).orElse(null);

            if (usuarioDB != null) {
                // Sincronizamos campos usando los nombres correctos de tu Entidad
                usuarioDB.setUsername(usuarioFormulario.getUsername()); // Cambiado de setNombre
                usuarioDB.setEmail(usuarioFormulario.getEmail());
                usuarioDB.setEstado(usuarioFormulario.getEstado());

                // Manejo de roles
                String rol = usuarioFormulario.getRol();
                if (rol != null && !rol.startsWith("ROLE_")) {
                    rol = "ROLE_" + rol;
                }
                usuarioDB.setRol(rol);

                // Actualizar contraseña solo si se envió una nueva
                if (usuarioFormulario.getPassword() != null && !usuarioFormulario.getPassword().isEmpty()) {
                    usuarioDB.setPassword(passwordEncoder.encode(usuarioFormulario.getPassword()));
                }

                usuarioService.guardar(usuarioDB);
                return "redirect:/usuarios";
            }
        }

        // --- MODO NUEVO (AUTO-INCREMENTO) ---
        // Al no entrar al IF anterior, el 'codigo' permanece null y la DB lo autoincrementa
        usuarioFormulario.setPassword(passwordEncoder.encode(usuarioFormulario.getPassword()));

        if (usuarioFormulario.getRol() != null && !usuarioFormulario.getRol().startsWith("ROLE_")) {
            usuarioFormulario.setRol("ROLE_" + usuarioFormulario.getRol());
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