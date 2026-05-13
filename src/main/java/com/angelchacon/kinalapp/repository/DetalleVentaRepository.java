package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    // 1. Buscar todos los detalles de una factura específica
    List<DetalleVenta> findByVentaCodigoVenta(Integer codigoVenta);

    // 2. Calcular el total de una venta sumando todos sus detalles
    @Query("SELECT SUM(d.subtotal) FROM DetalleVenta d WHERE d.venta.codigoVenta = :idVenta")
    Double sumTotalByVenta(@Param("idVenta") Integer idVenta);

    // 3. Buscar detalles por producto (útil para ver qué productos se venden más)
    List<DetalleVenta> findByProductoCodigoProducto(Integer codigoProducto);
}