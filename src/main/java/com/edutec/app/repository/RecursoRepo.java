package com.edutec.app.repository;

import com.edutec.app.domain.entity.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoRepo extends JpaRepository<Recurso, Integer> {

    // Usado por CatalogoController (sin orden)
    List<Recurso> findByAula_IdAula(Integer idAula);

    // Usado por vistas/listados donde quieres orden por nombre
    List<Recurso> findByAula_IdAulaOrderByNombreAsc(Integer idAula);

    // Para validar duplicados de nombre dentro de la misma aula (case-insensitive)
    boolean existsByAula_IdAulaAndNombreIgnoreCase(Integer idAula, String nombre);
}
