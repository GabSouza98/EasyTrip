package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Entity
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_anuncio")
    private Anuncio anuncio;

    @Embedded
    private Periodo periodo;

    private Integer quantidadePessoas;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataHoraReserva;

    @Embedded
    private Pagamento pagamento;

    @JsonIgnore
    private Boolean statusReserva;

    public Reserva() {

    }

    public Reserva(Long id, Usuario solicitante, Anuncio anuncio, Periodo periodo, Integer quantidadePessoas, LocalDateTime dataHoraReserva, Pagamento pagamento) {
        this.id = id;
        this.solicitante = solicitante;
        this.anuncio = anuncio;
        this.periodo = periodo;
        this.quantidadePessoas = quantidadePessoas;
        this.dataHoraReserva = dataHoraReserva;
        this.pagamento = pagamento;
        this.statusReserva = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Integer getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(Integer quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public LocalDateTime getDataHoraReserva() {
        return dataHoraReserva;
    }

    public void setDataHoraReserva(LocalDateTime dataHoraReserva) {
        this.dataHoraReserva = dataHoraReserva;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Boolean getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva() {

        if (this.pagamento.getStatus().equals(StatusPagamento.PAGO) || this.pagamento.getStatus().equals(StatusPagamento.PENDENTE)) {
            this.statusReserva = true;
        } else {
            this.statusReserva = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(id, reserva.id) && Objects.equals(solicitante, reserva.solicitante) && Objects.equals(anuncio, reserva.anuncio) && Objects.equals(periodo, reserva.periodo) && Objects.equals(quantidadePessoas, reserva.quantidadePessoas) && Objects.equals(pagamento, reserva.pagamento) && Objects.equals(statusReserva, reserva.statusReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, solicitante, anuncio, periodo, quantidadePessoas, dataHoraReserva, pagamento, statusReserva);
    }
}
