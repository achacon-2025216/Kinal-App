package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Usuario;
import com.angelchacon.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 1. LISTAR TODOS
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 2. LISTAR ACTIVOS
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByEstado(1);
    }

    // 3. GUARDAR (CREAR CUENTA)
    @Override
    public Usuario guardar(Usuario usuario) {
        // Asignación de valores por defecto para nuevos registros
        if (usuario.getEstado() == null) {
            usuario.setEstado(1);
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            usuario.setEmail("usuario@kinalapp.com");
        }

        // Lógica de Roles: Si no se especifica, es USER (solo lectura)
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            usuario.setRol("USER");
        } else {
            // Guardamos en minúsculas para facilitar comparaciones en el controlador
            usuario.setRol(usuario.getRol().toLowerCase());
        }

        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    // 4. BUSCAR POR ID/CÓDIGO
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Integer codigo) {
        return usuarioRepository.findById(codigo);
    }

    // 5. ACTUALIZAR
    @Override
    public Usuario actualizar(Integer codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código: " + codigo);
        }
        usuario.setCodigo(codigo); // Aseguramos que mantenga su ID original
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    // 6. ELIMINAR
    @Override
    public void eliminar(Integer codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("No se puede eliminar: Usuario no encontrado");
        }
        usuarioRepository.deleteById(codigo);
    }

    // 7. EXISTE POR CÓDIGO
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        return usuarioRepository.existsById(codigo);
    }

    // --- VALIDACIONES INTERNAS ---
    private void validarUsuario(Usuario usuario) {
        // Nota: El código ya no se valida aquí para permitir AUTO_INCREMENT en nuevos registros

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }
}