package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.repository.ProductoRepository;
import com.angelchacon.kinalapp.repository.VentaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository repository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public DetalleVentaService(DetalleVentaRepository repository,
                               ProductoRepository productoRepository,
                               VentaRepository ventaRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<DetalleVenta> listarTodos() {
        return repository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalle) {

        // Buscar producto real en la BD
        Producto producto = productoRepository.findById(
                detalle.getProducto().getCodigoProducto()
        ).orElse(null);

        // Buscar venta real en la BD
        Venta venta = ventaRepository.findById(
                detalle.getVenta().getCodigoVenta()
        ).orElse(null);

        // Asignarlos
        detalle.setProducto(producto);
        detalle.setVenta(venta);

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