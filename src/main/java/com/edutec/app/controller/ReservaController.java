package com.edutec.app.controller;

import com.edutec.app.controller.dto.DisponibilidadResponse;
import com.edutec.app.controller.dto.ReservaRequest;
import com.edutec.app.controller.dto.ReservaUpdateDTO;
import com.edutec.app.domain.entity.Reserva;
import com.edutec.app.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    private static boolean hasAuth(Authentication auth, String roleName) {
        if (auth == null) return false;
        String target = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase(target) || a.getAuthority().equalsIgnoreCase(roleName));
    }

    @GetMapping("/disponible")
    public DisponibilidadResponse disponible(
            @RequestParam int idAula,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime fin,
            @RequestParam(required = false, name = "idReservaExcl") Integer idReservaExcl) {

        boolean ok = reservaService.estaDisponibleExcluyendo(idAula, fecha, inicio, fin, idReservaExcl);
        return new DisponibilidadResponse(ok, ok ? "Disponible" : "Ocupado");
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ReservaRequest dto, Authentication auth) {
        String correo = auth.getName();
        Reserva r = reservaService.crearReserva(dto, correo);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Reserva creada",
                "estado", r.getEstado().getNombre(),
                "id_reserva", r.getIdReserva()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id,
                                        @RequestBody ReservaUpdateDTO dto,
                                        Authentication auth) {
        String correo = auth.getName();
        boolean isAdmin = hasAuth(auth, "ADMIN");
        Reserva r = reservaService.actualizarReserva(id, dto, correo, isAdmin);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Reserva actualizada",
                "id_reserva", r.getIdReserva(),
                "hora_inicio", r.getHoraInicio().toString(),
                "hora_fin", r.getHoraFin().toString()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable int id, Authentication auth) {
        String correo = auth.getName();
        boolean isAdmin = hasAuth(auth, "ADMIN");
        boolean isDocente = hasAuth(auth, "DOCENTE");
        reservaService.cancelarReserva(id, correo, isAdmin, isDocente);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
    }
}
