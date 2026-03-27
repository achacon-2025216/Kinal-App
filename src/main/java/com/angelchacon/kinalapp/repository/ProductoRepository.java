package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}