package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // --- RUTAS DE GESTIÓN (CON PREFIJO) ---

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

    @GetMapping("/usuarios/editar/{codigo}")
    public String editar(@PathVariable Integer codigo, Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listarTodos());
        model.addAttribute("usuarioEditando", usuarioService.buscarPorCodigo(codigo).orElse(new Usuario()));
        return "html/listarUsuario";
    }

    @GetMapping("/usuarios/eliminar/{codigo}")
    public String eliminar(@PathVariable Integer codigo) {
        usuarioService.eliminar(codigo);
        return "redirect:/usuarios";
    }

    // --- RUTAS DE AUTENTICACIÓN (LIMPIAS) ---

    @GetMapping("/login")
    public String mostrarLogin() {
        return "html/login";
    }

    @PostMapping("/login/entrar")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<Usuario> usuarioEncontrado = usuarioService.listarTodos().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (usuarioEncontrado.isPresent()) {
            session.setAttribute("usuarioActivo", usuarioEncontrado.get());
            return "redirect:/";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioNuevo", new Usuario());
        return "html/registro";
    }

    @PostMapping("/registro/guardar")
    public String registrarNuevo(@ModelAttribute("usuarioNuevo") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/login?success=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}