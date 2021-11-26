package io.github.cwireset.tcc.exception.anuncio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnuncioDuplicadoException extends Exception{
    public AnuncioDuplicadoException(Long id) {
        super(String.format("Já existe um recurso do tipo Anuncio com IdImovel com o valor '%d'.",id));
    }
}
