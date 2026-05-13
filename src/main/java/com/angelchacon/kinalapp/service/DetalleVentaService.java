package com.angelchacon.kinalapp.service;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import com.angelchacon.kinalapp.repository.DetalleVentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.angelchacon.kinalapp.entity.Producto;
import com.angelchacon.kinalapp.entity.Venta;
import com.angelchacon.kinalapp.repository.ProductoRepository;
import com.angelchacon.kinalapp.repository.VentaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // Garantiza que si falla la resta de stock, no se guarde el detalle
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository repository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public DetalleVentaService(DetalleVentaRepository repository, ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    // Constructor...

    @Override
    public List<DetalleVenta> listarTodos() {
        // CAMBIO: De List.of() a repository.findAll()
        return repository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalle) {
        // 1. Recuperar objetos persistidos para asegurar datos frescos
        Producto producto = productoRepository.findById(detalle.getProducto().getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Venta venta = ventaRepository.findById(detalle.getVenta().getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // 2. Validar y actualizar Stock
        if (producto.getStock() < detalle.getCantidad()) {
            throw new RuntimeException("No hay suficiente stock para: " + producto.getNombreProducto());
        }
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoRepository.save(producto);

        // 3. Actualizar el Total de la Venta (Cabecera)
        BigDecimal nuevoTotalVenta = venta.getTotal().add(detalle.getSubtotal());
        venta.setTotal(nuevoTotalVenta);
        ventaRepository.save(venta);

        // 4. Guardar el detalle con sus relaciones completas
        detalle.setProducto(producto);
        detalle.setVenta(venta);
        return repository.save(detalle);
    }

    @Override
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        // CAMBIO: De Optional.empty() a repository.findById(id)
        return repository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        DetalleVenta detalle = repository.findById(id).orElse(null);
        if (detalle != null) {
            // Lógica inversa: Devolver stock y restar del total de la venta
            Producto p = detalle.getProducto();
            p.setStock(p.getStock() + detalle.getCantidad());
            productoRepository.save(p);

            Venta v = detalle.getVenta();
            v.setTotal(v.getTotal().subtract(detalle.getSubtotal()));
            ventaRepository.save(v);

            repository.deleteById(id);
        }
    }
}