package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.imovel.ImovelAtreladoAnuncioAtivoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private AnuncioService anuncioService;

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
                cadastrarImovelRequest.getCaracteristicas(),
                true);

        imovelRepository.save(imovel);
        return imovel;
    }

//    public List<Imovel> listarImoveis() {
//        return imovelRepository.findByAtivoIsTrue();
//    }

    public Page<Imovel> listarImoveis(Pageable pageable) {
        return imovelRepository.findByAtivoIsTrue(pageable);
    }

    public List<Imovel> listarImoveisPorIdProprietario(Long id) {
        return imovelRepository.findByProprietarioIdAndAtivoIsTrue(id);
    }

    public Page<Imovel> listarImoveisPorIdProprietario(Long idProprietario, Pageable pageable) {
        return imovelRepository.findByProprietarioIdAndAtivoIsTrue(idProprietario, pageable);
    }

    public Imovel buscarImovelPorId(Long idImovel) throws ImovelNaoEncontradoException {

        Optional<Imovel> imovelProcurado = imovelRepository.findByIdAndAtivoIsTrue(idImovel);
        if(imovelProcurado.isPresent()) {
            return imovelProcurado.get();
        } else {
            throw new ImovelNaoEncontradoException(idImovel);
        }
    }

    public void excluirImovelPorId(Long idImovel) throws ImovelNaoEncontradoException, ImovelAtreladoAnuncioAtivoException {

        Imovel imovelProcurado = buscarImovelPorId(idImovel);
        verificaImovelAtreladoAnuncioAtivo(idImovel);
        imovelProcurado.setAtivo(false);
        imovelRepository.save(imovelProcurado);

    }

    public void verificaImovelAtreladoAnuncioAtivo(Long idImovel) throws ImovelAtreladoAnuncioAtivoException {

        if(anuncioService.buscarPorImovelId(idImovel)) {
            throw new ImovelAtreladoAnuncioAtivoException();
        }
    }

}
