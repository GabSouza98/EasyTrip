package io.github.cwireset.tcc.exception.usuario;

public class CpfInvalidoException extends Exception{

    public CpfInvalidoException() {
        super("O Cpf informado deve contar apenas dígitos, no formato 99999999999");
    }
}
