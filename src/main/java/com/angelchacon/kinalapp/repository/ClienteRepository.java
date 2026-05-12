package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Cliente;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    List<Cliente> findByEstado(int estado);

    // Agregamos el uso de CONCAT para asegurar que los comodines % se unan bien al término
    @Query("SELECT c FROM Cliente c WHERE c.nombreCliente LIKE CONCAT('%', :termino, '%') " +
            "OR c.dpiCliente LIKE CONCAT('%', :termino, '%') " +
            "OR c.apellidoCliente LIKE CONCAT('%', :termino, '%')")
    List<Cliente> buscarPorNombreODpi(@Param("termino") String termino);
}
