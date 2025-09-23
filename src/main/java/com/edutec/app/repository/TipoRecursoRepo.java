package com.edutec.app.repository;

import com.edutec.app.domain.entity.TipoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoRecursoRepo extends JpaRepository<TipoRecurso, Integer> {

    Optional<TipoRecurso> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
