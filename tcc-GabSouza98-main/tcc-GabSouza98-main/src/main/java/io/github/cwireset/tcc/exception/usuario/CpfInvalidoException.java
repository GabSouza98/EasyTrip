package io.github.cwireset.tcc.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfInvalidoException extends Exception{

    public CpfInvalidoException() {
        super("O Cpf informado deve contar apenas dígitos, no formato 99999999999");
    }
}
