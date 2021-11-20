package io.github.cwireset.tcc.exception.generica;

import io.github.cwireset.tcc.domain.TipoDominio;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TipoDominioNaoEncontradoException extends Exception {
    public TipoDominioNaoEncontradoException(TipoDominio tipoDominio, Long valor) {
        super(String.format("Nenhum(a) %s com Id com o valor '%d' foi encontrado.", tipoDominio.getNomeDominio(), valor));
    }
}
