package com.edutec.app.service;

import com.edutec.app.domain.entity.Reserva;

public interface NotificationService {
    void confirmacionCreacion(Reserva r);
    void confirmacionCambioHorario(Reserva r);
    void confirmacionCancelacion(Reserva r);
    void recordatorio(Reserva r);
}
