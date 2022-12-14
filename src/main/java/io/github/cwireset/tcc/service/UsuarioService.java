package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.TipoDominio;
import io.github.cwireset.tcc.exception.generica.TipoDominioNaoEncontradoException;
import io.github.cwireset.tcc.externalAPI.ClientFeign;
import io.github.cwireset.tcc.externalAPI.PostDTO;
import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.*;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClientFeign clientFeign;

    public Usuario cadastrarUsuario(Usuario usuarioRequest) throws Exception {

        if(!isNull(usuarioRepository.findByEmailEquals(usuarioRequest.getEmail()))){
            throw new EmailDuplicadoException(usuarioRequest.getEmail());
        }

        if(!isNull(usuarioRepository.findByCpfEquals(usuarioRequest.getCpf()))) {
            throw new CpfDuplicadoException(usuarioRequest.getCpf());
        }

        PostDTO postDTO = clientFeign.buscarLink();

        Usuario usuario = Usuario.builder()
                .id(null)
                .nome(usuarioRequest.getNome())
                .cpf(usuarioRequest.getCpf())
                .email(usuarioRequest.getEmail())
                .senha(usuarioRequest.getSenha())
                .dataNascimento(usuarioRequest.getDataNascimento())
                .endereco(usuarioRequest.getEndereco())
                .imagemAvatar(postDTO.getLink())
                .build();

        usuarioRepository.save(usuario);
        return usuario;
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Usuario buscarUsuarioPorId(Long idUsuario) throws TipoDominioNaoEncontradoException {
        Optional<Usuario> usuarioProcurado = usuarioRepository.findById(idUsuario);
        if(usuarioProcurado.isPresent()) {
            return usuarioProcurado.get();
        } else {
            throw new TipoDominioNaoEncontradoException(TipoDominio.USUARIO,idUsuario);
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

    public Usuario atualizarUsuario(Long id, AtualizarUsuarioRequest atualizarUsuarioRequest) throws EmailDuplicadoException, TipoDominioNaoEncontradoException {

        //Verificar se o ID existe
        Usuario usuarioProcurado = buscarUsuarioPorId(id);

        //Verifica se o email j?? existe, ou se o email n??o mudou
        if(!usuarioProcurado.getEmail().equals(atualizarUsuarioRequest.getEmail())) {
            if(!isNull(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail()))){
                throw new EmailDuplicadoException(atualizarUsuarioRequest.getEmail());
            }
        }

        Endereco enderecoNovo;
        //Verifica se foi passado um novo endere??o
        if(!isNull(atualizarUsuarioRequest.getEndereco())) {
            enderecoNovo = atualizarUsuarioRequest.getEndereco();
        } else {
            enderecoNovo = null;
        }

        //Verifica se o endere??o antigo existia, e se foi passado novo endere??o
        if(!isNull(usuarioProcurado.getEndereco()) && !isNull(enderecoNovo))  {
            enderecoNovo.setId(usuarioProcurado.getEndereco().getId());
        }

        Usuario usuarioAtualizado = new Usuario(id,
            atualizarUsuarioRequest.getNome(),
            atualizarUsuarioRequest.getEmail(),
            atualizarUsuarioRequest.getSenha(),
            usuarioProcurado.getCpf(),
            atualizarUsuarioRequest.getDataNascimento(),
            usuarioProcurado.getImagemAvatar(),
            enderecoNovo);

        usuarioRepository.save(usuarioAtualizado);

        return buscarUsuarioPorId(id);
    }


}
