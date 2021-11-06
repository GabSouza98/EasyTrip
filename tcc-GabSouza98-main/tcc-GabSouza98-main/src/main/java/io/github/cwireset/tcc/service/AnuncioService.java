package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.anuncio.AnuncioDuplicadoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Imovel imovel = imovelService.listarImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        Usuario anunciante = usuarioService.buscarUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());

        //Verifica se o imóvel já está sendo anunciado em outro anúncio
        if(!isNull(anuncioRepository.findByImovelId(cadastrarAnuncioRequest.getIdImovel()))) {
            throw new AnuncioDuplicadoException(cadastrarAnuncioRequest.getIdImovel());
        }

        Anuncio anuncio = new Anuncio(null,
                cadastrarAnuncioRequest.getTipoAnuncio(),
                imovel,
                anunciante,
                cadastrarAnuncioRequest.getValorDiaria(),
                cadastrarAnuncioRequest.getFormasAceitas(),
                cadastrarAnuncioRequest.getDescricao());

        anuncioRepository.save(anuncio);
        return anuncio;
    }
}
