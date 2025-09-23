package com.edutec.app.repository;

import com.edutec.app.domain.entity.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoReservaRepo extends JpaRepository<EstadoReserva, Integer> {
    Optional<EstadoReserva> findByNombre(String nombre);
}
