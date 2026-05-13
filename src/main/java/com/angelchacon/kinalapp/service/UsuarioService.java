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

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios(String termino) {
        return usuarioRepository.buscarPorNombreORol(termino);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        return usuarioRepository.findByEstado(1);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // 1. Si es un registro nuevo (código null)
        if (usuario.getCodigo() == null) {
            usuario.setEstado(1); // Activo por defecto

            // REGLA: Cualquier registro nuevo siempre será USER
            usuario.setRol("USER");

            if (usuario.getEmail() == null) {
                usuario.setEmail(usuario.getUsername() + "@kinalapp.com");
            }
        }

        validarRegistroBasico(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Integer codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    public Usuario actualizar(Integer codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        usuario.setCodigo(codigo);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Integer codigo) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con código " + codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Integer codigo) {
        return usuarioRepository.existsById(codigo);
    }

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