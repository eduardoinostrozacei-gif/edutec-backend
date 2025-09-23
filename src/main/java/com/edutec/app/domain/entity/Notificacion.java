package com.edutec.app.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @Column(nullable = false, length = 30)
    private String tipo; // CREACION | CAMBIO | CANCELACION | RECORDATORIO

    @Column(nullable = false, length = 120)
    private String correo;

    @Column(nullable = false, length = 160)
    private String asunto;

    @Column(nullable = false, length = 1200)
    private String cuerpo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "enviada", nullable = false)
    private boolean enviada = false;
}
