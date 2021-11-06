package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import io.github.cwireset.tcc.service.ImovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Imovel> listarImoveis() {
        return this.imovelService.listarImoveis();
    }

    @GetMapping("/proprietarios/{idProprietario}")
    public List<Imovel> listarImovelPorIdProprietario(@PathVariable Long idProprietario) {
        return this.imovelService.listarImoveisPorIdProprietario(idProprietario);
    }

    @GetMapping("/{idImovel}")
    public Imovel listarImovelPorId(@PathVariable Long idImovel) throws ImovelNaoEncontradoException {
        return this.imovelService.listarImovelPorId(idImovel);
    }

    @DeleteMapping("/{idImovel}")
    public void excluirImovel(@PathVariable Long idImovel) throws ImovelNaoEncontradoException {
        this.imovelService.excluirImovelPorId(idImovel);

    }



}
