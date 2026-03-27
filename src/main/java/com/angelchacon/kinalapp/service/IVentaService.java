package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    List<Venta> listarTodos();
    Venta guardar(Venta venta);
    Optional<Venta> buscarPorCodigo(Integer codigo);
    void eliminar(Integer codigo);
}