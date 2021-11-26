package io.github.cwireset.tcc.exception.reserva;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantidadeDiasInsuficienteException extends Exception{
    public QuantidadeDiasInsuficienteException(Integer dias, String tipoImovel) {
        super(String.format("Não é possivel realizar uma reserva com menos de %d diárias para imóveis do tipo %s", dias, tipoImovel));
    }
}
