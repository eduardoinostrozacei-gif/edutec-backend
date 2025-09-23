package com.edutec.app.repository;

import com.edutec.app.domain.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepo extends JpaRepository<Notificacion, Integer> { }
