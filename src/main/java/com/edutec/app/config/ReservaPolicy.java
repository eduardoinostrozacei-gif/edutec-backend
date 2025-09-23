package com.edutec.app.config;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ReservaPolicy {
    private ReservaPolicy(){}

    public static final LocalTime HORA_INICIO_PERMITIDA = LocalTime.of(8, 0);
    public static final LocalTime HORA_FIN_PERMITIDA    = LocalTime.of(20, 0);

    public static final Duration DURACION_MIN = Duration.ofMinutes(30);
    public static final Duration DURACION_MAX = Duration.ofHours(4);

    public static void validar(LocalDate fecha, LocalTime ini, LocalTime fin) {
        if (fecha == null || ini == null || fin == null)
            throw new IllegalArgumentException("fecha, horaInicio y horaFin son requeridas");
        if (!fin.isAfter(ini))
            throw new IllegalArgumentException("hora_fin debe ser posterior a hora_inicio");

        var ahora = LocalDateTime.now();
        var inicioDT = LocalDateTime.of(fecha, ini);
        if (inicioDT.isBefore(ahora))
            throw new IllegalArgumentException("No se permiten reservas en el pasado");

        if (ini.isBefore(HORA_INICIO_PERMITIDA) || fin.isAfter(HORA_FIN_PERMITIDA))
            throw new IllegalArgumentException(
                    "Horario permitido: " + HORA_INICIO_PERMITIDA + " a " + HORA_FIN_PERMITIDA);

        var dur = Duration.between(ini, fin);
        if (dur.compareTo(DURACION_MIN) < 0)
            throw new IllegalArgumentException("Duración mínima: " + DURACION_MIN.toMinutes() + " minutos");
        if (dur.compareTo(DURACION_MAX) > 0)
            throw new IllegalArgumentException("Duración máxima: " + DURACION_MAX.toHours() + " horas");
    }
}
