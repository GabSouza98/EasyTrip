package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.anuncio.AnuncioDuplicadoException;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ImovelService imovelService;



    public Anuncio cadastrarAnuncio(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws ImovelNaoEncontradoException, UsuarioNaoEncontradoException, AnuncioDuplicadoException {

        Imovel imovel = imovelService.buscarImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        Usuario anunciante = usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());

        //Verifica se o imóvel já está sendo anunciado em outro anúncio ativo
        if(!isNull(anuncioRepository.findByImovelIdAndImovelAtivoIsTrue(cadastrarAnuncioRequest.getIdImovel()))) {
            throw new AnuncioDuplicadoException(cadastrarAnuncioRequest.getIdImovel());
        }

        Anuncio anuncio = new Anuncio(null,
                cadastrarAnuncioRequest.getTipoAnuncio(),
                imovel,
                anunciante,
                cadastrarAnuncioRequest.getValorDiaria(),
                cadastrarAnuncioRequest.getFormasAceitas(),
                cadastrarAnuncioRequest.getDescricao(),
                true);

        anuncioRepository.save(anuncio);
        return anuncio;
    }

    public Boolean buscarPorImovelId(Long idImovel) {
        if(isNull(anuncioRepository.findByImovelIdAndAtivoIsTrue(idImovel))) {
            return false;
        } else {
            return true;
        }
    }

//    public List<Anuncio> listarAnuncios() {
//        return anuncioRepository.findByAtivoIsTrue();
//    }

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findByAtivoIsTrue(pageable);
    }

//    public List<Anuncio> listarAnunciosPorIdAnunciante(Long idAnunciante) {
//        return anuncioRepository.findByAnuncianteIdAndAtivoIsTrue(idAnunciante);
//    }

    public Page<Anuncio> listarAnunciosPorIdAnunciante(Long idAnunciante, Pageable pageable) {
        return anuncioRepository.findByAnuncianteIdAndAtivoIsTrue(idAnunciante, pageable);
    }

    public Anuncio buscarAnuncioPorId(Long id) throws AnuncioNaoEncontradoException {

        Optional<Anuncio> anuncioProcurado = anuncioRepository.findByIdAndAtivoIsTrue(id);
        if(anuncioProcurado.isPresent()) {
            return anuncioProcurado.get();
        } else {
            throw new AnuncioNaoEncontradoException(id);
        }
    }

    public void excluirAnuncioPorId(Long idAnuncio) throws AnuncioNaoEncontradoException {

        Anuncio anuncioParaExcluir = buscarAnuncioPorId(idAnuncio);
        anuncioParaExcluir.setAtivo(false);
        anuncioRepository.save(anuncioParaExcluir);
    }
}