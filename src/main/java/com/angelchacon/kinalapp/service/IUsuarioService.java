package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarTodos();
    List<Usuario> listarActivos();
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorCodigo(Integer codigo);
    Usuario actualizar(Integer codigo, Usuario usuario);
    void eliminar(Integer codigo);
    boolean existePorCodigo(Integer codigo);
}