package com.edutec.app.repository;

import com.edutec.app.domain.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaRepo extends JpaRepository<Reserva, Integer> {

    // -------- solapes (crear) SOLO reservas activas --------
    @Query("""
           select case when count(r) > 0 then true else false end
           from Reserva r
           where r.fecha = :fecha
             and r.aula.idAula = :idAula
             and r.estado.nombre = 'Activa'
             and (r.horaInicio < :hf and r.horaFin > :hi)
           """)
    boolean existeSolape(@Param("fecha") LocalDate fecha,
                         @Param("hi") LocalTime hi,
                         @Param("hf") LocalTime hf,
                         @Param("idAula") int idAula);

    // -------- solapes (editar) excluyendo la propia reserva, SOLO activas --------
    @Query("""
           select case when count(r) > 0 then true else false end
           from Reserva r
           where r.fecha = :fecha
             and r.aula.idAula = :idAula
             and r.estado.nombre = 'Activa'
             and r.idReserva <> :idReservaExcluir
             and (r.horaInicio < :hf and r.horaFin > :hi)
           """)
    boolean existeSolapeEdit(@Param("fecha") LocalDate fecha,
                             @Param("hi") LocalTime hi,
                             @Param("hf") LocalTime hf,
                             @Param("idAula") int idAula,
                             @Param("idReservaExcluir") int idReservaExcluir);

    // -------- listados del día --------
    List<Reserva> findByFechaOrderByHoraInicioAsc(LocalDate fecha);

    List<Reserva> findByFechaAndAula_IdAulaOrderByHoraInicioAsc(LocalDate fecha, Integer idAula);

    // -------- "mis reservas" (para el usuario autenticado) --------
    @Query("""
           select r
           from Reserva r
           where lower(r.usuario.correo) = lower(:correo)
           order by r.fecha desc, r.horaInicio desc
           """)
    List<Reserva> listarMias(@Param("correo") String correo);

    // -------- reportes (nativas) --------

    // uso de aulas por minutos reservados en rango de fechas
    @Query(value = """
        select a.id_aula       as aulaId,
               a.nombre        as aulaNombre,
               coalesce(sum(time_to_sec(timediff(r.hora_fin, r.hora_inicio)))/60, 0) as minutosReservados
        from aula a
        left join reserva r
               on r.id_aula = a.id_aula
              and r.fecha between :desde and :hasta
        group by a.id_aula, a.nombre
        order by a.id_aula
    """, nativeQuery = true)
    List<Object[]> usoAulas(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // cantidad por estado en rango de fechas
    @Query(value = """
        select e.nombre as estado,
               count(r.id_reserva) as cantidad
        from estado_reserva e
        left join reserva r
               on r.id_estado = e.id_estado
              and r.fecha between :desde and :hasta
        group by e.nombre
        order by e.nombre
    """, nativeQuery = true)
    List<Object[]> estadosMes(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
