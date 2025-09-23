package com.edutec.app.repository;

import com.edutec.app.domain.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepo extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
