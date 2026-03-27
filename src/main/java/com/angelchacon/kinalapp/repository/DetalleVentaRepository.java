package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
}