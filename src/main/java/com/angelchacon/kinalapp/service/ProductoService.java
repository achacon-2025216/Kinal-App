package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> buscarPorCodigo(Integer codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public void eliminar(Integer codigo) {
        productoRepository.deleteById(codigo);
    }
}