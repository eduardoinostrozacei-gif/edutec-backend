package com.edutec.app.controller;

import com.edutec.app.controller.dto.RecursoDTO;
import com.edutec.app.domain.entity.Recurso;
import com.edutec.app.service.RecursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/recursos") // <— CAMBIADO PARA EVITAR CHOQUE
@RequiredArgsConstructor
public class RecursoController {

    private final RecursoService service;

    // Listado por aula (admin)
    @GetMapping
    public List<Recurso> listarPorAula(@RequestParam Integer idAula) {
        return service.listarPorAula(idAula);
    }

    // Crear (admin)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RecursoDTO dto) {
        var r = service.crear(dto);
        return ResponseEntity.ok(Map.of("mensaje","Creado","id_recurso", r.getIdRecurso()));
    }

    // Actualizar (admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody RecursoDTO dto) {
        var r = service.actualizar(id, dto);
        return ResponseEntity.ok(Map.of("mensaje","Actualizado","id_recurso", r.getIdRecurso()));
    }

    // Eliminar (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje","Eliminado"));
    }
}
