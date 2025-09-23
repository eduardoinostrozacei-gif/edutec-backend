package com.edutec.app.controller.dto;

import lombok.Data;

@Data
public class ReservaUpdateDTO {
    private String horaInicio;  // HH:mm:ss
    private String horaFin;     // HH:mm:ss
}
