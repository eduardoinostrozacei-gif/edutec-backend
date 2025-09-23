package com.edutec.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ReporteEstadoMesDTO {
    private String estado;
    private long cantidad;
}
