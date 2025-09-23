package com.edutec.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ReporteUsoAulaDTO {
    private int aulaId;
    private String aulaNombre;
    private long minutosReservados;
}
