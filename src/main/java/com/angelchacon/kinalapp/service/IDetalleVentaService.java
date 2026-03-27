package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarTodos();
    DetalleVenta guardar(DetalleVenta detalle);
    Optional<DetalleVenta> buscarPorId(Integer id);
    void eliminar(Integer id);
}