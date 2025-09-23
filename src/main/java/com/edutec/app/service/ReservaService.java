package com.edutec.app.service;

import com.edutec.app.controller.dto.ReservaRequest;
import com.edutec.app.controller.dto.ReservaUpdateDTO;
import com.edutec.app.domain.entity.Aula;
import com.edutec.app.domain.entity.EstadoReserva;
import com.edutec.app.domain.entity.Reserva;
import com.edutec.app.domain.entity.Usuario;
import com.edutec.app.repository.AulaRepo;
import com.edutec.app.repository.EstadoReservaRepo;
import com.edutec.app.repository.ReservaRepo;
import com.edutec.app.repository.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepo reservaRepo;
    private final AulaRepo aulaRepo;
    private final UsuarioRepo usuarioRepo;
    private final EstadoReservaRepo estadoReservaRepo;

    @Transactional(readOnly = true)
    public boolean estaDisponible(int idAula, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        if (!inicio.isBefore(fin)) return false; // [inicio, fin)
        return !reservaRepo.existeSolape(fecha, inicio, fin, idAula);
    }

    @Transactional(readOnly = true)
    public boolean estaDisponibleExcluyendo(int idAula, LocalDate fecha, LocalTime inicio, LocalTime fin, Integer idReservaExcluir) {
        if (!inicio.isBefore(fin)) return false;
        if (idReservaExcluir == null) return estaDisponible(idAula, fecha, inicio, fin);
        return !reservaRepo.existeSolapeEdit(fecha, inicio, fin, idAula, idReservaExcluir);
    }

    @Transactional
    public Reserva crearReserva(ReservaRequest dto, String correo) {
        Aula aula = aulaRepo.findById(dto.getIdAula())
                .orElseThrow(() -> new IllegalArgumentException("Aula no existe"));

        Usuario usuario = usuarioRepo.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        LocalDate fecha = LocalDate.parse(dto.getFecha());
        LocalTime hi = LocalTime.parse(dto.getHoraInicio());
        LocalTime hf = LocalTime.parse(dto.getHoraFin());

        if (hi.compareTo(hf) >= 0) {
            throw new IllegalStateException("Rango horario inválido (inicio >= fin)");
        }

        if (!estaDisponible(aula.getIdAula(), fecha, hi, hf)) {
            throw new IllegalStateException("No se puede reservar: existe solapamiento en ese rango");
        }

        EstadoReserva estadoActiva = estadoReservaRepo.findByNombre("Activa")
                .orElseThrow(() -> new IllegalStateException("Estado 'Activa' no disponible"));

        Reserva r = new Reserva();
        r.setFecha(fecha);
        r.setHoraInicio(hi);
        r.setHoraFin(hf);
        r.setAula(aula);
        r.setUsuario(usuario);
        r.setEstado(estadoActiva);

        return reservaRepo.save(r);
    }

    @Transactional
    public Reserva actualizarReserva(int id, ReservaUpdateDTO dto, String correo, boolean isAdmin) {
        Reserva r = reservaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Permitir si es el dueño o si es ADMIN
        boolean esDueno = r.getUsuario().getCorreo().equalsIgnoreCase(correo);
        if (!esDueno && !isAdmin) {
            throw new IllegalStateException("No autorizado para actualizar esta reserva");
        }

        LocalTime hi = LocalTime.parse(dto.getHoraInicio());
        LocalTime hf = LocalTime.parse(dto.getHoraFin());
        if (hi.compareTo(hf) >= 0) {
            throw new IllegalStateException("Rango horario inválido (inicio >= fin)");
        }

        boolean haySolape = !estaDisponibleExcluyendo(
                r.getAula().getIdAula(), r.getFecha(), hi, hf, r.getIdReserva());
        if (haySolape) {
            throw new IllegalStateException("No se puede actualizar: existe solape en ese rango");
        }

        r.setHoraInicio(hi);
        r.setHoraFin(hf);
        return reservaRepo.save(r);
    }

    @Transactional
    public void cancelarReserva(int id, String correo, boolean isAdmin, boolean isDocente) {
        Reserva r = reservaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Permitir si es el dueño, o si es ADMIN o DOCENTE (según lógica del front)
        boolean esDueno = r.getUsuario().getCorreo().equalsIgnoreCase(correo);
        if (!esDueno && !(isAdmin || isDocente)) {
            throw new IllegalStateException("No autorizado para cancelar esta reserva");
        }

        EstadoReserva cancelada = estadoReservaRepo.findByNombre("Cancelada")
                .orElseThrow(() -> new IllegalStateException("Estado 'Cancelada' no disponible"));

        r.setEstado(cancelada);
        reservaRepo.save(r);
    }
}
