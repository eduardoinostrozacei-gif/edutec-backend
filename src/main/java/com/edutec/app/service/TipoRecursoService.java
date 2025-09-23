package com.edutec.app.service;

import com.edutec.app.controller.dto.TipoRecursoDTO;
import com.edutec.app.domain.entity.TipoRecurso;
import com.edutec.app.repository.TipoRecursoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoRecursoService {

    private final TipoRecursoRepo repo;

    @Transactional(readOnly = true)
    public List<TipoRecurso> listar() {
        return repo.findAll();
    }

    @Transactional
    public TipoRecurso crear(TipoRecursoDTO dto) {
        if (dto == null || dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        String nombre = dto.getNombre().trim();
        if (repo.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalStateException("Tipo de recurso duplicado");
        }
        TipoRecurso tr = new TipoRecurso();
        tr.setNombre(nombre);
        return repo.save(tr);
    }

    @Transactional
    public TipoRecurso actualizar(Integer id, TipoRecursoDTO dto) {
        TipoRecurso tr = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TipoRecurso no existe"));

        if (dto == null || dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        String nombre = dto.getNombre().trim();
        if (!tr.getNombre().equalsIgnoreCase(nombre) && repo.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalStateException("Tipo de recurso duplicado");
        }
        tr.setNombre(nombre);
        return repo.save(tr);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("TipoRecurso no existe");
        }
        repo.deleteById(id);
    }
}
