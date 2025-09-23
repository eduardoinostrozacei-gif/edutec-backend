package com.edutec.app.controller.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaDTO {
    private Integer idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer aulaId;
    private String aulaNombre;
    private String estado;
    private String usuarioCorreo;
}
