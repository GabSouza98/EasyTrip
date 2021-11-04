package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.*;
import io.github.cwireset.tcc.repository.UsuarioRepository;
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

    public Usuario cadastrarUsuario(Usuario usuarioRequest) throws Exception {

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


    public Usuario buscarUsuarioPorCpf(String cpf) throws CpfNaoEncontradoException {

        Usuario usuarioProcurado = usuarioRepository.findByCpfEquals(cpf);

        if(isNull(usuarioProcurado)){
            throw new CpfNaoEncontradoException(cpf);
        } else {
            return usuarioProcurado;
        }
    }
}
