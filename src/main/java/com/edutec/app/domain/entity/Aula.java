package com.edutec.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity @Table(name = "aula")
public class Aula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Integer idAula;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer capacidad;

    private String ubicacion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_aula", nullable = false)
    private TipoAula tipoAula;
}

