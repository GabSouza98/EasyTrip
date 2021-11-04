package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.*;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.validator.ValidacaoUsuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    ModelMapper modelMapper = new ModelMapper();

    public Usuario cadastrarUsuario(Usuario usuarioRequest) throws CpfInvalidoException, CepInvalidoException, EmailDuplicadoException, CpfDuplicadoException {

        new ValidacaoUsuario().accept(usuarioRequest);

        if(!isNull(usuarioRepository.findByEmailEquals(usuarioRequest.getEmail()))) {
            throw new EmailDuplicadoException(usuarioRequest.getEmail());
        }
        if(!isNull(usuarioRepository.findByCpfEquals(usuarioRequest.getCpf()))) {
            throw new CpfDuplicadoException(usuarioRequest.getCpf());
        }

        Usuario usuario = modelMapper.map(usuarioRequest, Usuario.class);
        usuarioRepository.save(usuario);
        return usuario;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long idUsuario) throws UsuarioNaoEncontradoException {
        Optional<Usuario> usuarioProcurado = usuarioRepository.findById(idUsuario);
        if(usuarioProcurado.isPresent()) {
            return usuarioProcurado.get();
        } else {
            throw new UsuarioNaoEncontradoException(idUsuario);
        }

    }
}
