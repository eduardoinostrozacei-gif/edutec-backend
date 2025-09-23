package com.edutec.app.controller.dto;

import lombok.Data;

@Data
public class ReservaRequest {
    private int idAula;
    private String fecha;       // yyyy-MM-dd
    private String horaInicio;  // HH:mm:ss
    private String horaFin;     // HH:mm:ss
}
