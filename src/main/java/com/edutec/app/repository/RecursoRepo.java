package com.edutec.app.repository;

import com.edutec.app.domain.entity.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoRepo extends JpaRepository<Recurso, Integer> {


    List<Recurso> findByAula_IdAula(Integer idAula);


    List<Recurso> findByAula_IdAulaOrderByNombreAsc(Integer idAula);


    boolean existsByAula_IdAulaAndNombreIgnoreCase(Integer idAula, String nombre);
}
