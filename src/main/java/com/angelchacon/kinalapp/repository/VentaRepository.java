package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    // Método para buscar coincidencias por nombre del cliente o ID de venta
    @Query("SELECT v FROM Venta v WHERE v.cliente.nombreCliente LIKE %:t% OR CAST(v.codigoVenta AS string) LIKE %:t%")
    List<Venta> buscarPorClienteOCodigo(@Param("t") String termino);
}