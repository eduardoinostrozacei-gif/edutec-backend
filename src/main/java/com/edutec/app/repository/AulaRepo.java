package com.edutec.app.repository;

import com.edutec.app.domain.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AulaRepo extends JpaRepository<Aula, Integer> {
    Optional<Aula> findByNombre(String nombre);
}
