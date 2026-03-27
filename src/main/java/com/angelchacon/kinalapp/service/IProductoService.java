package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarTodos();
    Producto guardar(Producto producto);
    Optional<Producto> buscarPorCodigo(Integer codigo);
    void eliminar(Integer codigo);
}