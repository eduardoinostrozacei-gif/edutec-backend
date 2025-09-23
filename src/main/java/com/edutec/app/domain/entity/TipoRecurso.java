package com.edutec.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity @Table(name = "tipo_recurso")
public class TipoRecurso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_recurso")
    private Integer idTipoRecurso;

    @Column(nullable = false, unique = true)
    private String nombre;
}
