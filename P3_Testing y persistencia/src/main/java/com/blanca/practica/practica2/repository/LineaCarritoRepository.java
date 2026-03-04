package com.blanca.practica.practica2.repository;

import com.blanca.practica.practica2.model.LineaCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineaCarritoRepository extends JpaRepository<LineaCarrito, Long> {
    List<LineaCarrito> findByCarrito_IdCarrito(Long idCarrito);
}