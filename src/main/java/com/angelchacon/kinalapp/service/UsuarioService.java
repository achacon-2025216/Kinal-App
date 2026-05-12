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

    // --- IMPLEMENTACIÓN DEL BUSCADOR ---
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios(String termino) {
        // Lógica para filtrar por username o rol usando el repositorio
        return usuarioRepository.buscarPorNombreORol(termino);
    }

    // Devuelve todos los usuarios
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Devuelve solo los usuarios activos (estado = 1)
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByEstado(1);
    }

    // Guarda un usuario nuevo o actualiza si ya existe
    @Override
    public Usuario guardar(Usuario usuario) {
        // 1. Si es un usuario nuevo (código null), asignamos valores por defecto
        if (usuario.getCodigo() == null) {
            usuario.setEstado(1); // Activo por defecto

            if (usuario.getRol() == null) {
                usuario.setRol("ROLE_USER");
            }
            if (usuario.getEmail() == null) {
                usuario.setEmail(usuario.getUsername() + "@kinalapp.com");
            }
        }

        // 2. Validamos Username y Password
        validarRegistroBasico(usuario);

        return usuarioRepository.save(usuario);
    }

    // Busca un usuario por su código
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Integer codigo) {
        return usuarioRepository.findById(codigo);
    }

    // Actualiza los datos de un usuario existente
    @Override
    public Usuario actualizar(Integer codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        usuario.setCodigo(codigo);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    // Elimina un usuario por su código
    @Override
    public void eliminar(Integer codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    // Verifica si un usuario existe por su código
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        return usuarioRepository.existsById(codigo);
    }

    // --- MÉTODOS DE VALIDACIÓN PRIVADOS ---

    private void validarRegistroBasico(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es necesario");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es necesaria");
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }
}