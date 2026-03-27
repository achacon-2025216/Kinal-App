package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository repository;

    public DetalleVentaService(DetalleVentaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DetalleVenta> listarTodos() {
        return repository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalle) {
        return repository.save(detalle);
    }

    @Override
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }
}