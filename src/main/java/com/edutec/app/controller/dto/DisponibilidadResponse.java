package com.edutec.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibilidadResponse {
    private boolean disponible;
    private String mensaje;
}
