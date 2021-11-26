package io.github.cwireset.tcc.exception.reserva;

import io.github.cwireset.tcc.domain.FormaPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormaPagamentoInvalidaException extends Exception {
    public FormaPagamentoInvalidaException(String mensagemErro) {
        super(mensagemErro);
    }
}
