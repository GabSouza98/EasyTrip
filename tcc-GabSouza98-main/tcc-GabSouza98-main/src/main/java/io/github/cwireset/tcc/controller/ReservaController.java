package io.github.cwireset.tcc.controller;


import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.reserva.DataSaidaMenorQueDataEntradaException;
import io.github.cwireset.tcc.exception.reserva.NumeroMinimoDiariasException;
import io.github.cwireset.tcc.exception.reserva.SolicitanteIgualAnuncianteException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse cadastrarReserva(@Valid @RequestBody CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        return this.reservaService.cadastrarReserva(cadastrarReservaRequest);
    }

    @GetMapping("/solicitantes/{idSolicitante}")
    public List<Reserva> consultarReservasPorSolicitante(@PathVariable Long idSolicitante, Periodo periodo) throws UsuarioNaoEncontradoException {
        return this.reservaService.consultarReservasPorSolicitante(idSolicitante, periodo);

    }
}
