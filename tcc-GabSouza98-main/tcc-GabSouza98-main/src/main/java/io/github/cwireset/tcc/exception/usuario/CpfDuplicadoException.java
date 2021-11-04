package io.github.cwireset.tcc.exception.usuario;

public class CpfDuplicadoException extends Exception {

    public CpfDuplicadoException(String cpf) {
        super(String.format("Já existe um recurso do tipo Usuario com CPF com o valor '%s'.",cpf));
    }
}
