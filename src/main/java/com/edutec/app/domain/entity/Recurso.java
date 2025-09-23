package com.edutec.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity @Table(name = "recurso")
public class Recurso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Integer idRecurso;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_tipo_recurso", nullable = false)
    private TipoRecurso tipoRecurso;

    @ManyToOne
    @JoinColumn(name = "id_aula", nullable = false)
    private Aula aula;
}
