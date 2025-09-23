package com.edutec.app.controller;

import com.edutec.app.controller.dto.TipoRecursoDTO;
import com.edutec.app.domain.entity.TipoRecurso;
import com.edutec.app.service.TipoRecursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tipos-recurso") // <— CAMBIADO PARA EVITAR CHOQUE
@RequiredArgsConstructor
public class TipoRecursoController {

    private final TipoRecursoService service;

    @GetMapping
    public List<TipoRecurso> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoRecursoDTO dto) {
        var tr = service.crear(dto);
        return ResponseEntity.ok(Map.of("mensaje","Creado","id_tipo_recurso", tr.getIdTipoRecurso()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TipoRecursoDTO dto) {
        var tr = service.actualizar(id, dto);
        return ResponseEntity.ok(Map.of("mensaje","Actualizado","id_tipo_recurso", tr.getIdTipoRecurso()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje","Eliminado"));
    }
}
