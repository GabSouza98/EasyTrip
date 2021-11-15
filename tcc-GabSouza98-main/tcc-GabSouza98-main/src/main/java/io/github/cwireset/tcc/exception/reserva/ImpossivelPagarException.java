package io.github.cwireset.tcc.exception.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImpossivelPagarException extends Exception{
    public ImpossivelPagarException() {
        super("Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.");
    }
}
