package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.*;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
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

        if(!isNull(usuarioRepository.findByEmailEquals(usuarioRequest.getEmail()))){
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

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws UsuarioNaoEncontradoException, CpfDuplicadoException, EmailDuplicadoException {

        //Verificar se o ID existe
        Usuario usuarioProcurado = buscarUsuarioPorId(id);

        //Verifica se o email já existe, ou se o email não mudou
        if(!usuarioProcurado.getEmail().equals(atualizarUsuarioRequest.getEmail())) {
            if(!isNull(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail()))){
                throw new EmailDuplicadoException(atualizarUsuarioRequest.getEmail());
            }
        }

        Endereco enderecoNovo;
        //Verifica se foi passado um novo endereço
        if(!isNull(atualizarUsuarioRequest.getEndereco())) {
            enderecoNovo = atualizarUsuarioRequest.getEndereco();
        } else {
            enderecoNovo = usuarioProcurado.getEndereco();
        }

        //Verifica se o endereço antigo existia, e define o ID do novo endereço como sendo do antigo
        if(!isNull(usuarioProcurado.getEndereco())){
            enderecoNovo.setId(usuarioProcurado.getEndereco().getId());
        }

        Usuario usuarioAtualizado = new Usuario(id,
            atualizarUsuarioRequest.getNome(),
            atualizarUsuarioRequest.getEmail(),
            atualizarUsuarioRequest.getSenha(),
            usuarioProcurado.getCpf(),
            atualizarUsuarioRequest.getDataNascimento(),
            enderecoNovo);

        usuarioRepository.save(usuarioAtualizado);
        return buscarUsuarioPorId(id);
    }
}
