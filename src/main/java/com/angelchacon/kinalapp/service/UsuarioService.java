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

    // Repositorio que maneja los datos de Usuario
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
        validarUsuario(usuario); // valida que los campos obligatorios estén completos
        if (usuario.getEstado() == 0 || usuario.getEstado() == 0) {
            usuario.setEstado(1); // por defecto lo activa
        }
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

    // Validaciones internas: revisa que los campos obligatorios estén completos
    private void validarUsuario(Usuario usuario) {
        if (usuario.getCodigo() == null || usuario.getCodigo() <= 0) {
            throw new IllegalArgumentException("El código es obligatorio y debe ser mayor que 0");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }
}