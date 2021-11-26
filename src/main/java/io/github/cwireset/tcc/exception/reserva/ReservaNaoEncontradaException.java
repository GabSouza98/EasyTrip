package io.github.cwireset.tcc.exception.reserva;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservaNaoEncontradaException extends Exception{
    public ReservaNaoEncontradaException(Long id) {
        super(String.format("Nenhum(a) Reserva com Id com o valor '%d' foi encontrado.", id));
    }
}
