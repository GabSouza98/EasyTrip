package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ImovelService {

    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private UsuarioService usuarioService;

    ModelMapper modelMapper = new ModelMapper();

    public Imovel cadastrarImovel(CadastrarImovelRequest cadastrarImovelRequest) throws UsuarioNaoEncontradoException {

        Usuario proprietario;

        if(isNull(usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario()))){
            throw new UsuarioNaoEncontradoException(cadastrarImovelRequest.getIdProprietario());
        } else {
            proprietario = usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario());
        }

        Imovel imovel = new Imovel(null,
                cadastrarImovelRequest.getIdentificacao(),
                cadastrarImovelRequest.getTipoImovel(),
                cadastrarImovelRequest.getEndereco(),
                proprietario,
                cadastrarImovelRequest.getCaracteristicas());

        imovelRepository.save(imovel);
        return imovel;
    }

    public List<Imovel> listarImoveis() {
        return imovelRepository.findAll();
    }

    public List<Imovel> listarImoveisPorIdProprietario(Long id) {
        return imovelRepository.findByProprietarioId(id);
    }

    public Imovel listarImovelPorId(Long idImovel) throws ImovelNaoEncontradoException {

        Optional<Imovel> imovelProcurado = imovelRepository.findById(idImovel);
        if(imovelProcurado.isPresent()) {
            return imovelProcurado.get();
        } else {
            throw new ImovelNaoEncontradoException(idImovel);
        }
    }

    public void excluirImovelPorId(Long idImovel) throws ImovelNaoEncontradoException {

        Imovel imovelProcurado = listarImovelPorId(idImovel);
        imovelRepository.delete(imovelProcurado);

    }
}
