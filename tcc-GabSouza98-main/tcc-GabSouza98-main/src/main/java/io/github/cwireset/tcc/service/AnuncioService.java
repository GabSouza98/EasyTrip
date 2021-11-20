package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.TipoDominio;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.anuncio.AnuncioDuplicadoException;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.generica.TipoDominioNaoEncontradoException;
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

    public Anuncio cadastrarAnuncio(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws AnuncioDuplicadoException, TipoDominioNaoEncontradoException {

        Imovel imovel;
        Usuario anunciante;

        if(isNull(imovelService.buscarImovelPorId(cadastrarAnuncioRequest.getIdImovel()))) {
            throw new TipoDominioNaoEncontradoException(TipoDominio.IMOVEL, cadastrarAnuncioRequest.getIdImovel());
        } else {
            imovel = imovelService.buscarImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        }

        if(isNull(usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante()))) {
            throw new TipoDominioNaoEncontradoException(TipoDominio.USUARIO, cadastrarAnuncioRequest.getIdAnunciante());
        } else {
            anunciante = usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());
        }

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

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findByAtivoIsTrue(pageable);
    }

    public Page<Anuncio> listarAnunciosPorIdAnunciante(Long idAnunciante, Pageable pageable) {
        return anuncioRepository.findByAnuncianteIdAndAtivoIsTrue(idAnunciante, pageable);
    }

    public Anuncio buscarAnuncioPorId(Long id) throws TipoDominioNaoEncontradoException {

        Optional<Anuncio> anuncioProcurado = anuncioRepository.findByIdAndAtivoIsTrue(id);
        if(anuncioProcurado.isPresent()) {
            return anuncioProcurado.get();
        } else {
            throw new TipoDominioNaoEncontradoException(TipoDominio.ANUNCIO, id);
        }
    }

    public void excluirAnuncioPorId(Long idAnuncio) throws TipoDominioNaoEncontradoException {
        Anuncio anuncioParaExcluir = buscarAnuncioPorId(idAnuncio);
        anuncioParaExcluir.setAtivo(false);
        anuncioRepository.save(anuncioParaExcluir);
    }
}
