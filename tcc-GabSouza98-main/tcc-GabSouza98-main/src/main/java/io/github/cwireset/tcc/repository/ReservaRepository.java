package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends CrudRepository<Reserva,Long> {

    //Usadas em CadastrarReserva
    List<Reserva> findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraFinalBetween(Long id, LocalDateTime d1, LocalDateTime d2);
    List<Reserva> findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialBetween(Long id, LocalDateTime d1, LocalDateTime d2);
    List<Reserva> findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialBeforeAndPeriodoDataHoraFinalAfter(Long id, LocalDateTime d1, LocalDateTime d2);

    List<Reserva> findBySolicitanteId(Long id);
    Page<Reserva> findBySolicitanteId(Long id, Pageable pageable);

    List<Reserva> findBySolicitanteIdAndPeriodoDataHoraFinalBetween(Long id, LocalDateTime d1, LocalDateTime d2);
    List<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialBetween(Long id, LocalDateTime d1, LocalDateTime d2);
    List<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialBeforeAndPeriodoDataHoraFinalAfter(Long id, LocalDateTime d1, LocalDateTime d2);

    List<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialAfterAndPeriodoDataHoraFinalBefore(Long id, LocalDateTime d1, LocalDateTime d2);

    List<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Long id, LocalDateTime d1, LocalDateTime d2);
    Page<Reserva> findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(Long id, LocalDateTime d1, LocalDateTime d2, Pageable pageable);

    List<Reserva> findByAnuncioAnuncianteId(Long id);

}
