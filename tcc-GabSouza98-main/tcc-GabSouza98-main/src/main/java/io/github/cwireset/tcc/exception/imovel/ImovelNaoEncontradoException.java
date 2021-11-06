package io.github.cwireset.tcc.exception.imovel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImovelNaoEncontradoException extends Exception{
    public ImovelNaoEncontradoException(Long id) {
        super(String.format("Nenhum(a) Imovel com Id com o valor '%d' foi encontrado.", id));
    }
}
