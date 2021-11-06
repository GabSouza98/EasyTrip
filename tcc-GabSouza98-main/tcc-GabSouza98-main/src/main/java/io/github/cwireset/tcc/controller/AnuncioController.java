package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Anuncio cadastrarAnuncio(@Valid @RequestBody CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        return this.anuncioService.cadastrarAnuncio(cadastrarAnuncioRequest);
    }

    @GetMapping
    public List<Anuncio> listarAnuncios() {
        return this.anuncioService.listarAnuncios();
    }

    @GetMapping("/anunciantes/{idAnunciante}")
    public List<Anuncio> listarAnunciosPorIdAnunciante(@PathVariable Long idAnunciante) {
        return this.anuncioService.listarAnunciosPorIdAnunciante(idAnunciante);
    }

    @DeleteMapping("/{idAnuncio}")
    public void excluirAnuncio(@PathVariable Long idAnuncio) throws AnuncioNaoEncontradoException {
        this.anuncioService.excluirAnuncioPorId(idAnuncio);
    }



}
