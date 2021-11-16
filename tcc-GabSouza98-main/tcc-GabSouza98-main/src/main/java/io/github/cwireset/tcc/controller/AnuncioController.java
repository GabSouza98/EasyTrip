package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public Page<Anuncio> listarAnuncios(@PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable) {
        return this.anuncioService.listarAnuncios(pageable);
    }

    @GetMapping("/anunciantes/{idAnunciante}")
    public Page<Anuncio> listarAnunciosPorIdAnunciante(@PageableDefault(sort = "valorDiaria", direction = Sort.Direction.ASC) @ApiIgnore Pageable pageable,
                                                       @PathVariable Long idAnunciante) {
        return this.anuncioService.listarAnunciosPorIdAnunciante(idAnunciante, pageable);
    }

    @DeleteMapping("/{idAnuncio}")
    public void excluirAnuncio(@PathVariable Long idAnuncio) throws AnuncioNaoEncontradoException {
        this.anuncioService.excluirAnuncioPorId(idAnuncio);
    }



}
