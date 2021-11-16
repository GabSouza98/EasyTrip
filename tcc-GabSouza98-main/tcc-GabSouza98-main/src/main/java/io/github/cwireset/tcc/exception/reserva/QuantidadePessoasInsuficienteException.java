package io.github.cwireset.tcc.exception.reserva;

import io.github.cwireset.tcc.domain.TipoImovel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantidadePessoasInsuficienteException extends Exception{
    public QuantidadePessoasInsuficienteException(Integer numeroPessoas, String tipoImovel) {
        super(String.format("Não é possivel realizar uma reserva com menos de %d pessoas para imóveis do tipo %s", numeroPessoas, tipoImovel));
    }
}
