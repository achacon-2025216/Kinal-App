package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCodigo(String codigo) {
        return ventaRepository.findById(codigo);
    }

    @Override
    public void eliminar(String codigo) {
        ventaRepository.deleteById(codigo);
    }
}