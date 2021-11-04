package io.github.cwireset.tcc.exception.usuario;

public class CepInvalidoException extends Exception{

    public CepInvalidoException() {
        super("O Cep deve ser no formato 99999-999");
    }
}
