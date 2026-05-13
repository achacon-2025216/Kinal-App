package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Listar solo productos activos (estado = 1)
    List<Producto> findByEstado(Integer estado);

    // Buscador profesional por nombre
    @Query("SELECT p FROM Producto p WHERE p.nombreProducto LIKE %:t% AND p.estado = 1")
    List<Producto> buscarPorNombre(@Param("t") String termino);
}