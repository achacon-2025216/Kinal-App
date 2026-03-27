package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}