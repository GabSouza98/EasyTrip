package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.exception.imovel.ImovelAtreladoAnuncioAtivoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Imovel cadastrarImovel(@Valid @RequestBody CadastrarImovelRequest cadastrarImovelRequest) throws UsuarioNaoEncontradoException {
        return this.imovelService.cadastrarImovel(cadastrarImovelRequest);
    }

    @GetMapping
    public Page<Imovel> listarImoveis(@PageableDefault(sort = "identificacao") @ApiIgnore Pageable pageable) {
        return this.imovelService.listarImoveis(pageable);
    }

    @GetMapping("/proprietarios/{idProprietario}")
    public List<Imovel> listarImovelPorIdProprietario(@PathVariable Long idProprietario) {
        return this.imovelService.listarImoveisPorIdProprietario(idProprietario);
    }

    @GetMapping("/{idImovel}")
    public Imovel listarImovelPorId(@PathVariable Long idImovel) throws ImovelNaoEncontradoException {
        return this.imovelService.buscarImovelPorId(idImovel);
    }

    @DeleteMapping("/{idImovel}")
    public void excluirImovel(@PathVariable Long idImovel) throws ImovelNaoEncontradoException, ImovelAtreladoAnuncioAtivoException {
        this.imovelService.excluirImovelPorId(idImovel);

    }



}
