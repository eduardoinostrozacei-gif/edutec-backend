package com.edutec.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity @Table(name = "tipo_aula")
public class TipoAula {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_aula")
    private Integer idTipoAula;

    @Column(nullable = false, unique = true)
    private String nombre; // en tu BD no hay descripcion
}
