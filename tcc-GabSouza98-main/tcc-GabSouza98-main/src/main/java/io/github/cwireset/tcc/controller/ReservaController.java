package io.github.cwireset.tcc.controller;


import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Periodo;
import io.github.cwireset.tcc.domain.Reserva;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.reserva.*;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import io.github.cwireset.tcc.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public Page<Reserva> consultarReservasPorSolicitante(@PageableDefault(sort = "periodo.dataHoraFinal", direction = Sort.Direction.DESC) @ApiIgnore Pageable pageable,
                                                         @PathVariable Long idSolicitante, Periodo periodo) throws UsuarioNaoEncontradoException {
        return this.reservaService.consultarReservasPorSolicitante(idSolicitante, periodo, pageable);
    }

    @GetMapping("/anuncios/anunciantes/{idAnunciante}")
    public List<Reserva> consultarReservasPorAnunciante(@PathVariable Long idAnunciante) {
        return this.reservaService.consultarReservasPorAnunciante(idAnunciante);
    }

    @PutMapping("/{idReserva}/pagamentos")
    public void pagarReserva(@PathVariable Long idReserva, @Valid @RequestBody FormaPagamento formaPagamento) throws ReservaNaoEncontradaException, FormaPagamentoInvalidaException, ImpossivelPagarException {
        this.reservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping("/{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) throws ReservaNaoEncontradaException, ImpossivelCancelarException {
        this.reservaService.cancelarReserva(idReserva);
    }

    @PutMapping("/{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) throws ReservaNaoEncontradaException, ImpossivelEstornarException {
        this.reservaService.estornarReserva(idReserva);
    }


}
