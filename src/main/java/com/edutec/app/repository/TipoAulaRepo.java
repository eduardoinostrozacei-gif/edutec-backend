package com.edutec.app.repository;

import com.edutec.app.domain.entity.TipoAula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoAulaRepo extends JpaRepository<TipoAula, Integer> {
    Optional<TipoAula> findByNombre(String nombre);
}
