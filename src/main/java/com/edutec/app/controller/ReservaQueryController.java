package com.edutec.app.controller;

import com.edutec.app.controller.dto.ReservaDTO;
import com.edutec.app.domain.entity.Reserva;
import com.edutec.app.repository.ReservaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas-query")
@RequiredArgsConstructor
public class ReservaQueryController {

    private final ReservaRepo reservaRepo;

    // GET /api/reservas-query/listar?fecha=2025-09-23&idAula=1&page=0&size=10
    @GetMapping("/listar")
    public Page<ReservaDTO> listar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Integer idAula,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Reserva> list = (idAula == null)
                ? reservaRepo.findByFechaOrderByHoraInicioAsc(fecha)
                : reservaRepo.findByFechaAndAula_IdAulaOrderByHoraInicioAsc(fecha, idAula);

        int start = Math.min(page * size, list.size());
        int end   = Math.min(start + size, list.size());
        var content = list.subList(start, end).stream().map(this::toDTO).toList();
        return new PageImpl<>(content, PageRequest.of(page, size), list.size());
    }

    // GET /api/reservas-query/mias/page?page=0&size=10
    @GetMapping("/mias/page")
    public Page<ReservaDTO> mias(Authentication auth,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        String correo = auth.getName();
        List<Reserva> list = reservaRepo.listarMias(correo);

        int start = Math.min(page * size, list.size());
        int end   = Math.min(start + size, list.size());
        var content = list.subList(start, end).stream().map(this::toDTO).toList();
        return new PageImpl<>(content, PageRequest.of(page, size), list.size());
    }

    private ReservaDTO toDTO(Reserva r) {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(r.getIdReserva());
        dto.setFecha(r.getFecha());
        dto.setHoraInicio(r.getHoraInicio());
        dto.setHoraFin(r.getHoraFin());
        dto.setAulaId(r.getAula().getIdAula());
        dto.setAulaNombre(r.getAula().getNombre());
        dto.setEstado(r.getEstado() != null ? r.getEstado().getNombre() : null);
        dto.setUsuarioCorreo(r.getUsuario() != null ? r.getUsuario().getCorreo() : null);
        return dto;
    }
}
