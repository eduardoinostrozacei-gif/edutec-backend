package com.edutec.app.service;

import com.edutec.app.controller.dto.RecursoDTO;
import com.edutec.app.domain.entity.Aula;
import com.edutec.app.domain.entity.Recurso;
import com.edutec.app.domain.entity.TipoRecurso;
import com.edutec.app.repository.AulaRepo;
import com.edutec.app.repository.RecursoRepo;
import com.edutec.app.repository.TipoRecursoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoService {

    private final RecursoRepo recursos;
    private final TipoRecursoRepo tipos;
    private final AulaRepo aulas;

    @Transactional(readOnly = true)
    public List<Recurso> listarPorAula(Integer idAula) {
        return recursos.findByAula_IdAulaOrderByNombreAsc(idAula);
    }

    @Transactional
    public Recurso crear(RecursoDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Datos requeridos");
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("Nombre requerido");
        if (dto.getIdAula() == null) throw new IllegalArgumentException("idAula requerido");
        if (dto.getIdTipoRecurso() == null) throw new IllegalArgumentException("idTipoRecurso requerido");

        Aula aula = aulas.findById(dto.getIdAula())
                .orElseThrow(() -> new IllegalArgumentException("Aula no existe"));
        TipoRecurso tipo = tipos.findById(dto.getIdTipoRecurso())
                .orElseThrow(() -> new IllegalArgumentException("TipoRecurso no existe"));

        String nombre = dto.getNombre().trim();
        if (recursos.existsByAula_IdAulaAndNombreIgnoreCase(aula.getIdAula(), nombre)) {
            throw new IllegalStateException("Recurso duplicado en esta aula");
        }

        Recurso r = new Recurso();
        r.setNombre(nombre);
        r.setAula(aula);
        r.setTipoRecurso(tipo);
        return recursos.save(r);
    }

    @Transactional
    public Recurso actualizar(Integer id, RecursoDTO dto) {
        Recurso r = recursos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no existe"));

        // nombre
        if (dto.getNombre() != null) {
            String nombre = dto.getNombre().trim();
            if (nombre.isEmpty()) throw new IllegalArgumentException("Nombre requerido");
            // validar duplicado en aula actual
            if (!nombre.equalsIgnoreCase(r.getNombre())
                    && recursos.existsByAula_IdAulaAndNombreIgnoreCase(r.getAula().getIdAula(), nombre)) {
                throw new IllegalStateException("Recurso duplicado en esta aula");
            }
            r.setNombre(nombre);
        }

        // tipo
        if (dto.getIdTipoRecurso() != null
                && (r.getTipoRecurso() == null
                || !dto.getIdTipoRecurso().equals(r.getTipoRecurso().getIdTipoRecurso()))) {
            TipoRecurso nuevoTipo = tipos.findById(dto.getIdTipoRecurso())
                    .orElseThrow(() -> new IllegalArgumentException("TipoRecurso no existe"));
            r.setTipoRecurso(nuevoTipo);
        }

        // mover de aula (opcional)
        if (dto.getIdAula() != null && !dto.getIdAula().equals(r.getAula().getIdAula())) {
            Aula nuevaAula = aulas.findById(dto.getIdAula())
                    .orElseThrow(() -> new IllegalArgumentException("Aula no existe"));
            // si cambias de aula, valida duplicado en la nueva
            String nombreActual = r.getNombre();
            if (recursos.existsByAula_IdAulaAndNombreIgnoreCase(nuevaAula.getIdAula(), nombreActual)) {
                throw new IllegalStateException("Recurso duplicado en la nueva aula");
            }
            r.setAula(nuevaAula);
        }

        return recursos.save(r);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!recursos.existsById(id)) {
            throw new IllegalArgumentException("Recurso no existe");
        }
        recursos.deleteById(id);
    }
}
