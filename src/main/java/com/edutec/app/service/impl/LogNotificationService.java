package com.edutec.app.service.impl;

import com.edutec.app.domain.entity.Notificacion;
import com.edutec.app.domain.entity.Reserva;
import com.edutec.app.repository.NotificacionRepo;
import com.edutec.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogNotificationService implements NotificationService {

    private final NotificacionRepo repo;

    @Transactional
    void store(String tipo, String correo, String asunto, String cuerpo) {
        var n = new Notificacion();
        n.setTipo(tipo);
        n.setCorreo(correo);
        n.setAsunto(asunto);
        n.setCuerpo(cuerpo);
        n.setEnviada(true); // marcamos como "enviada/registrada"
        repo.save(n);
        log.info("[NOTIF:{}] to={} | {} - {}", tipo, correo, asunto, cuerpo);
    }

    @Override
    public void confirmacionCreacion(Reserva r) {
        String c = r.getUsuario().getCorreo();
        store("CREACION", c, "Reserva confirmada",
                "Tu reserva #" + r.getIdReserva() + " para el " + r.getFecha() +
                        " (" + r.getHoraInicio() + " - " + r.getHoraFin() + ") fue creada.");
    }

    @Override
    public void confirmacionCambioHorario(Reserva r) {
        String c = r.getUsuario().getCorreo();
        store("CAMBIO", c, "Reserva reprogramada",
                "Tu reserva #" + r.getIdReserva() + " fue reprogramada a " + r.getFecha() +
                        " (" + r.getHoraInicio() + " - " + r.getHoraFin() + ").");
    }

    @Override
    public void confirmacionCancelacion(Reserva r) {
        String c = r.getUsuario().getCorreo();
        store("CANCELACION", c, "Reserva cancelada",
                "Tu reserva #" + r.getIdReserva() + " ha sido cancelada.");
    }

    @Override
    public void recordatorio(Reserva r) {
        String c = r.getUsuario().getCorreo();
        store("RECORDATORIO", c, "Recordatorio de reserva",
                "Recordatorio: reserva #" + r.getIdReserva() + " hoy " + r.getHoraInicio() +
                        " - " + r.getHoraFin() + " en aula " + r.getAula().getNombre());
    }
}
