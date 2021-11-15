package io.github.cwireset.tcc.exception.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImpossivelCancelarException extends Exception {
    public ImpossivelCancelarException() {
        super("Não é possível realizar o cancelamento para esta reserva, pois ela não está no status PENDENTE.");
    }
}
