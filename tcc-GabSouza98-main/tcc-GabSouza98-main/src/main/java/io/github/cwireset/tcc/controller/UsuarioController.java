package io.github.cwireset.tcc.controller;

import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.CpfInvalidoException;
import io.github.cwireset.tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Usuario> listarUsuarios() {
        return this.usuarioService.listarUsuarios();
    }




}
