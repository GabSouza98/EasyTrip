package io.github.cwireset.tcc.validator;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.CepInvalidoException;
import io.github.cwireset.tcc.exception.usuario.CpfInvalidoException;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class ValidacaoUsuario {

    public void accept(Usuario usuarioRequest) throws CpfInvalidoException, CepInvalidoException {

        if(!usuarioRequest.getCpf().matches(("\\d{11}"))) {
            throw new CpfInvalidoException();
        }

        if(!isNull(usuarioRequest.getEndereco())) {
            if(!usuarioRequest.getEndereco().getCep().matches(("\\d{5}-\\d{3}"))) {
                throw new CepInvalidoException();
            }
        }

    }

}
