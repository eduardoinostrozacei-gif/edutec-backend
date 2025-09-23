package com.edutec.app.controller;

import com.edutec.app.controller.dto.ReporteEstadoMesDTO;
import com.edutec.app.controller.dto.ReporteUsoAulaDTO;
import com.edutec.app.repository.ReservaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','DOCENTE')") // <-- solo ADMIN y DOCENTE
public class ReporteController {

    private final ReservaRepo reservaRepo;

    @GetMapping("/uso-aulas")
    public List<ReporteUsoAulaDTO> usoAulas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return reservaRepo.usoAulas(desde, hasta).stream()
                .map(r -> new ReporteUsoAulaDTO(
                        ((Number) r[0]).intValue(),
                        (String) r[1],
                        r[2] == null ? 0L : ((Number) r[2]).longValue()
                ))
                .toList();
    }

    @GetMapping("/estados-mes")
    public List<ReporteEstadoMesDTO> estadosMes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return reservaRepo.estadosMes(desde, hasta).stream()
                .map(r -> new ReporteEstadoMesDTO(
                        (String) r[0],
                        ((Number) r[1]).longValue()
                ))
                .toList();
    }
}
