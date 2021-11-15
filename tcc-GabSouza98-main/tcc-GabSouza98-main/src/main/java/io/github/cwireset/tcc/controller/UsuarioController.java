package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.CpfDuplicadoException;
import io.github.cwireset.tcc.exception.usuario.CpfNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.EmailDuplicadoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import io.github.cwireset.tcc.service.UsuarioService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrarUsuario(@Valid @RequestBody Usuario usuarioRequest) throws Exception {
        return this.usuarioService.cadastrarUsuario(usuarioRequest);
    }

    @GetMapping
    public Page<Usuario> listarUsuarios(@PageableDefault(sort = "nome") @ApiIgnore Pageable pageable) {
        return this.usuarioService.listarUsuarios(pageable);
    }

    @GetMapping("/{idUsuario}")
    public Usuario buscarUsuarioPorId(@PathVariable Long idUsuario) throws Exception {
        return this.usuarioService.buscarUsuarioPorId(idUsuario);
    }

    @GetMapping("/cpf/{cpf}")
    public Usuario buscarUsuarioPorCpf(@PathVariable String cpf) throws CpfNaoEncontradoException {
        return this.usuarioService.buscarUsuarioPorCpf(cpf);
    }

    @PutMapping("/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @Valid @RequestBody AtualizarUsuarioRequest atualizarUsuarioRequest) throws Exception {
        return this.usuarioService.atualizarUsuario(id, atualizarUsuarioRequest);
    }




}
