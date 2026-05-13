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
        // Mostramos solo los que no están eliminados lógicamente
        return productoRepository.findByEstado(1);
    }

    @Override
    public Producto guardar(Producto producto) {
        // Si es un producto nuevo, forzamos el estado a 1
        if (producto.getEstado() == null) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> buscarPorCodigo(Integer codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public void eliminar(Integer codigo) {
        // Borrado lógico: buscamos el producto y cambiamos su estado a 0
        productoRepository.findById(codigo).ifPresent(p -> {
            p.setEstado(0);
            productoRepository.save(p);
        });
    }

    @Override
    public List<Producto> buscarProductos(String termino) {
        return productoRepository.buscarPorNombre(termino);
    }
}