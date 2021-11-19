package io.github.cwireset.tcc.repository;

import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

//@Repository
//public class ReservaRepositoryDao {
//
//    EntityManager em;
//
//    public ReservaRepositoryDao(EntityManager em) {
//        this.em = em;
//    }
//
//    public List<Reserva> EncontraReservasConflitantes(Long id, LocalDateTime d1, LocalDateTime d2) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery cq = cb.createQuery(Reserva.class);
//
//        Root<Reserva> reserva = cq.from(Reserva.class);
//        Predicate anuncioId = cb.equal(reserva.get("anuncio").get("id"), id);
//
//        Predicate statusPagamentoPago = cb.equal(reserva.get("pagamento").get("status"), StatusPagamento.PAGO);
//        Predicate statusPagamentoPendente = cb.equal(reserva.get("pagamento").get("status"), StatusPagamento.PENDENTE);
//        Predicate statusPagamento = cb.or(statusPagamentoPago,statusPagamentoPendente);
//
//        //FUNCIONA, porém nao é possivel reservar no mesmo dia que acaba/começa uma outra reserva...
////        Predicate dataHoraInicial = cb.between(reserva.get("periodo").get("dataHoraInicial"),d1,d2);
////        Predicate dataHoraFinal = cb.between(reserva.get("periodo").get("dataHoraFinal"),d1,d2);
//        Predicate dataHoraInicial = cb.greaterThanOrEqualTo(reserva.get("periodo").get("dataHoraInicial"),d2);
//        Predicate dataHoraFinal = cb.lessThanOrEqualTo(reserva.get("periodo").get("dataHoraFinal"),d1);
//        Predicate dataHoraConflito = cb.or(dataHoraInicial,dataHoraFinal);
//
//        Predicate conflito = cb.and(anuncioId,statusPagamento,dataHoraConflito);
//
//        cq.where(conflito);
//        List<Reserva> reservas = em.createQuery(cq).getResultList();
//
//        return reservas;
//    }
//
//}
