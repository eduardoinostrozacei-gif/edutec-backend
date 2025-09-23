package com.edutec.app.controller;

import com.edutec.app.domain.entity.Aula;
import com.edutec.app.domain.entity.Recurso;
import com.edutec.app.domain.entity.TipoAula;
import com.edutec.app.domain.entity.TipoRecurso;
import com.edutec.app.repository.AulaRepo;
import com.edutec.app.repository.RecursoRepo;
import com.edutec.app.repository.TipoAulaRepo;
import com.edutec.app.repository.TipoRecursoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {

    private final AulaRepo aulaRepo;
    private final TipoAulaRepo tipoAulaRepo;
    private final TipoRecursoRepo tipoRecursoRepo;
    private final RecursoRepo recursoRepo;

    // ======================= AULAS =======================

    // GET /api/aulas?page=0&size=10  -> autenticados
    @GetMapping("/aulas")
    public Page<Aula> aulas(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        return aulaRepo.findAll(PageRequest.of(page, size, Sort.by("idAula").ascending()));
    }

    // GET /api/aulas/{id} -> autenticados
    @GetMapping("/aulas/{id}")
    public Aula aulaPorId(@PathVariable Integer id){
        return aulaRepo.findById(id).orElseThrow();
    }

    // DTO para crear/editar aula
    public record AulaDTO(String nombre, Integer capacidad, String ubicacion, Integer idTipoAula) {}

    // POST /api/aulas -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/aulas")
    public Aula crearAula(@RequestBody AulaDTO dto){
        Aula a = new Aula();
        mapearAula(a, dto);
        return aulaRepo.save(a);
    }

    // PUT /api/aulas/{id} -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/aulas/{id}")
    public Aula actualizarAula(@PathVariable Integer id, @RequestBody AulaDTO dto){
        Aula a = aulaRepo.findById(id).orElseThrow();
        mapearAula(a, dto);
        return aulaRepo.save(a);
    }

    // DELETE /api/aulas/{id} -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/aulas/{id}")
    public void eliminarAula(@PathVariable Integer id){
        aulaRepo.deleteById(id);
    }

    private void mapearAula(Aula a, AulaDTO dto){
        a.setNombre(dto.nombre());
        a.setCapacidad(dto.capacidad());
        a.setUbicacion(dto.ubicacion());
        TipoAula ta = tipoAulaRepo.findById(dto.idTipoAula()).orElseThrow();
        a.setTipoAula(ta);
    }

    // ================= Tipos de Aula / Recurso ================

    // GET /api/tipos-aula -> autenticados
    @GetMapping("/tipos-aula")
    public List<TipoAula> tiposAula(){
        return tipoAulaRepo.findAll(Sort.by("nombre"));
    }

    // GET /api/tipos-recurso -> autenticados
    @GetMapping("/tipos-recurso")
    public List<TipoRecurso> tiposRecurso(){
        return tipoRecursoRepo.findAll(Sort.by("nombre"));
    }

    // ====================== RECURSOS ======================

    // GET /api/recursos (?aulaId=1) -> autenticados
    @GetMapping("/recursos")
    public List<Recurso> recursos(@RequestParam(required = false) Integer aulaId){
        if (aulaId == null) return recursoRepo.findAll();
        return recursoRepo.findByAula_IdAula(aulaId);
    }

    // GET /api/recursos/{id} -> autenticados
    @GetMapping("/recursos/{id}")
    public Recurso recursoPorId(@PathVariable Integer id){
        return recursoRepo.findById(id).orElseThrow();
    }

    // DTO para crear/editar recurso
    public record RecursoDTO(String nombre, Integer idTipoRecurso, Integer idAula) {}

    // POST /api/recursos -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/recursos")
    public Recurso crearRecurso(@RequestBody RecursoDTO dto){
        Recurso r = new Recurso();
        mapearRecurso(r, dto);
        return recursoRepo.save(r);
    }

    // PUT /api/recursos/{id} -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/recursos/{id}")
    public Recurso actualizarRecurso(@PathVariable Integer id, @RequestBody RecursoDTO dto){
        Recurso r = recursoRepo.findById(id).orElseThrow();
        mapearRecurso(r, dto);
        return recursoRepo.save(r);
    }

    // DELETE /api/recursos/{id} -> ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/recursos/{id}")
    public void eliminarRecurso(@PathVariable Integer id){
        recursoRepo.deleteById(id);
    }

    private void mapearRecurso(Recurso r, RecursoDTO dto){
        r.setNombre(dto.nombre());
        r.setTipoRecurso(tipoRecursoRepo.findById(dto.idTipoRecurso()).orElseThrow());
        r.setAula(aulaRepo.findById(dto.idAula()).orElseThrow());
    }
}
