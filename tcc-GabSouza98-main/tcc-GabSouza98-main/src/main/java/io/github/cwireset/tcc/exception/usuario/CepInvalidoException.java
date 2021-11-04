package io.github.cwireset.tcc.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CepInvalidoException extends Exception{

    public CepInvalidoException() {
        super("O Cep deve ser no formato 99999-999");
    }
}
