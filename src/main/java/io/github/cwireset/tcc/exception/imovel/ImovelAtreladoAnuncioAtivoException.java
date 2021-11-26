package io.github.cwireset.tcc.exception.imovel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImovelAtreladoAnuncioAtivoException extends Exception {
    public ImovelAtreladoAnuncioAtivoException() {
        super("Não é possível excluir um imóvel que possua um anúncio.");
    }
}
