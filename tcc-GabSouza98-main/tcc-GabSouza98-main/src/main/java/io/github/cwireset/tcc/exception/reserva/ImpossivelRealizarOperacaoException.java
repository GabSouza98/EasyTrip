package io.github.cwireset.tcc.exception.reserva;

import io.github.cwireset.tcc.domain.StatusPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImpossivelRealizarOperacaoException extends Exception{
    public ImpossivelRealizarOperacaoException(StatusPagamento status1, StatusPagamento status2) {
        super(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status %s.",status1.getOperacao(),status2.getNomeStatus()));
    }
}
