package com.angelchacon.kinalapp.repository;

import com.angelchacon.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByEstado(int estado);
    Usuario findByUsername(String username);

    // NUEVO: Método para buscar por nombre de usuario o rol
    @Query("SELECT u FROM Usuario u WHERE u.username LIKE %:t% OR u.rol LIKE %:t%")
    List<Usuario> buscarPorNombreORol(@Param("t") String termino);
}