package io.github.cwireset.tcc.exception.usuario;

public class EmailDuplicadoException extends Exception{
    public EmailDuplicadoException(String email) {
        super(String.format("Já existe um recurso do tipo Usuario com E-Mail com o valor '%s'.", email));
    }
}
