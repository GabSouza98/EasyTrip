package io.github.cwireset.tcc.exception.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumeroMinimoDiariasException extends Exception{
    public NumeroMinimoDiariasException() {
        super("Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");
    }
}
