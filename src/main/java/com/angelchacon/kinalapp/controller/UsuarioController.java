package com.angelchacon.kinalapp.controller;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.service.IUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Listar todos
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    // Listar activos
    @GetMapping("/activos")
    public List<Usuario> listarActivos() {
        return usuarioService.listarActivos();
    }

    // Buscar por código
    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable Integer codigo) {
        Optional<Usuario> usuario = usuarioService.buscarPorCodigo(codigo);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Guardar
    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.guardar(usuario);
        return ResponseEntity.ok(nuevo);
    }

    // Actualizar
    @PutMapping("/{codigo}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer codigo, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(codigo, usuario);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer codigo) {
        usuarioService.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}