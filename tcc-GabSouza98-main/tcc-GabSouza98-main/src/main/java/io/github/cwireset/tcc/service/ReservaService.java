package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.reserva.*;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;

import io.github.cwireset.tcc.repository.ReservaRepository;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;

import static java.util.Objects.isNull;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioService anuncioService;

//    @Autowired
//    private ReservaRepositoryDao reservaRepositoryDao;

    public InformacaoReservaResponse cadastrarReserva(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {

        LocalDateTime dataHoraReserva = LocalDateTime.now();

        Usuario solicitante = usuarioService.buscarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());
        Anuncio anuncio = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());

        Periodo periodoModificado = modificaHoraPeriodo(cadastrarReservaRequest.getPeriodo());
        BigDecimal valorTotal = calculaValorTotal(periodoModificado, anuncio.getValorDiaria());

        //mudei para o periodo nao modificado
        if(periodoModificado.getDataHoraFinal().isBefore(periodoModificado.getDataHoraInicial())) {
            throw new DataSaidaMenorQueDataEntradaException();
        }

        //mudei para o periodo nao modificado
        if(calculaDias(periodoModificado)<1) {
            throw new NumeroMinimoDiariasException();
        }

        if(cadastrarReservaRequest.getIdSolicitante() == anuncio.getAnunciante().getId()) {
            throw new SolicitanteIgualAnuncianteException();
        }

        //LEGADO. Deixei aqui para lembrar que isso existe, apenas.
//        List<Reserva> reservasConflitantes = anuncioRepositoryDao.EncontraReservasConflitantes(cadastrarReservaRequest.getIdAnuncio(),cadastrarReservaRequest.getPeriodo().getDataHoraInicial(),cadastrarReservaRequest.getPeriodo().getDataHoraFinal());
//        if(!reservasConflitantes.isEmpty()) {
//            throw new ConflitoAnuncioException();
//        }

        List<Reserva> listaConflitos = reservaRepository.findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(cadastrarReservaRequest.getIdAnuncio(),periodoModificado.getDataHoraFinal(),periodoModificado.getDataHoraInicial());
        if(!listaConflitos.isEmpty()) {
            throw new ConflitoAnuncioException();
        }

        if(anuncio.getImovel().getTipoImovel().equals(TipoImovel.HOTEL)) {
            if(cadastrarReservaRequest.getQuantidadePessoas() < 2) {
                throw new QuantidadePessoasInsuficienteException(2, TipoImovel.HOTEL.getNome());
            }
        }

        if(anuncio.getImovel().getTipoImovel().equals(TipoImovel.POUSADA)) {
            Integer dias = calculaDias(cadastrarReservaRequest.getPeriodo());
            if(dias<5){
                throw new QuantidadeDiasInsuficienteException(5, TipoImovel.POUSADA.getNome());
            }
        }

        Reserva reserva = new Reserva(null,
                solicitante,
                anuncio,
                periodoModificado,
                cadastrarReservaRequest.getQuantidadePessoas(),
                dataHoraReserva,
                new Pagamento(valorTotal,null, StatusPagamento.PENDENTE));

        reservaRepository.save(reserva);

        InformacaoReservaResponse informacaoReservaResponse = new InformacaoReservaResponse(
                reserva.getId(),
                new DadosSolicitanteResponse(solicitante.getId(), solicitante.getNome()),
                cadastrarReservaRequest.getQuantidadePessoas(),
                new DadosAnuncioResponse(anuncio.getId(),
                        anuncio.getImovel(),
                        anuncio.getAnunciante(),
                        anuncio.getFormasAceitas(),
                        anuncio.getDescricao()),
                periodoModificado,
                new Pagamento(valorTotal,null, StatusPagamento.PENDENTE));

        return informacaoReservaResponse;
    }

    public Periodo modificaHoraPeriodo(Periodo periodo) {

        LocalDateTime dataHoraEntradaOriginal = periodo.getDataHoraInicial();
        LocalDateTime dataHoraSaidaOriginal = periodo.getDataHoraFinal();

        LocalDate dataEntrada = LocalDate.of(dataHoraEntradaOriginal.getYear(),
                dataHoraEntradaOriginal.getMonth(),
                dataHoraEntradaOriginal.getDayOfMonth());

        LocalDate dataSaida = LocalDate.of(dataHoraSaidaOriginal.getYear(),
                dataHoraSaidaOriginal.getMonth(),
                dataHoraSaidaOriginal.getDayOfMonth());

        LocalTime horaEntrada = LocalTime.of(14,0);
        LocalTime horaSaida = LocalTime.of(12,0);

        LocalDateTime dataHoraEntradaModificada = LocalDateTime.of(dataEntrada,horaEntrada);
        LocalDateTime dataHoraSaidaModificada = LocalDateTime.of(dataSaida,horaSaida);

        Periodo periodoModificado = new Periodo();
        periodoModificado.setDataHoraInicial(dataHoraEntradaModificada);
        periodoModificado.setDataHoraFinal(dataHoraSaidaModificada);

        return periodoModificado;
    }

    public Integer calculaDias(Periodo periodo) {

        LocalDateTime dataHoraEntradaOriginal = periodo.getDataHoraInicial();
        LocalDateTime dataHoraSaidaOriginal = periodo.getDataHoraFinal();

        LocalDate dataEntrada = LocalDate.of(dataHoraEntradaOriginal.getYear(),
                dataHoraEntradaOriginal.getMonth(),
                dataHoraEntradaOriginal.getDayOfMonth());

        LocalDate dataSaida = LocalDate.of(dataHoraSaidaOriginal.getYear(),
                dataHoraSaidaOriginal.getMonth(),
                dataHoraSaidaOriginal.getDayOfMonth());

        return Period.between(dataEntrada,dataSaida).getDays();
    }

    public BigDecimal calculaValorTotal(Periodo periodo, BigDecimal valorDiaria) {
        Integer quantidadeDias = calculaDias(periodo).intValue();
        BigDecimal temporario = new BigDecimal(quantidadeDias);
        return valorDiaria.multiply(temporario);
    }

    public Page<Reserva> consultarReservasPorSolicitante(Long idSolicitante, Periodo periodo, Pageable pageable) {

        //Verifica se foi informado o período
        if(isNull(periodo)) {
            return reservaRepository.findBySolicitanteId(idSolicitante, pageable);
        }

        //Verifica se o período informado está completo
        if(!isNull(periodo)){
            if(isNull(periodo.getDataHoraFinal()) || isNull(periodo.getDataHoraInicial())) {
                return reservaRepository.findBySolicitanteId(idSolicitante, pageable);
            }
        }

        Page<Reserva> reservasFiltradas = reservaRepository.findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(idSolicitante,periodo.getDataHoraInicial(),periodo.getDataHoraFinal(), pageable);
        return reservasFiltradas;
    }

//    public List<Reserva> consultarReservasPorAnunciante(Long idAnunciante) {
//        return reservaRepository.findByAnuncioAnuncianteId(idAnunciante);
//    }

    public Page<Reserva> consultarReservasPorAnunciante(Long idAnunciante, Pageable pageable) {
        return reservaRepository.findByAnuncioAnuncianteId(idAnunciante, pageable);
    }

    public Reserva buscarReservaPorId(Long id) throws ReservaNaoEncontradaException {

        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()){
            return reserva.get();
        } else {
            throw new ReservaNaoEncontradaException(id);
        }

    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws ReservaNaoEncontradaException, FormaPagamentoInvalidaException, ImpossivelPagarException {

        Reserva reserva = buscarReservaPorId(idReserva);

        List<FormaPagamento> formasAceitas = reserva.getAnuncio().getFormasAceitas();

        if(formasAceitas.contains(formaPagamento)) {

            if(reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)) {
                reserva.getPagamento().setStatus(StatusPagamento.PAGO);
                reserva.getPagamento().setFormaEscolhida(formaPagamento);
                reserva.setStatusReserva();
                reservaRepository.save(reserva);
            } else {
                throw new ImpossivelPagarException();
            }

        } else {
            String mensagemErro = String.format("O anúncio não aceita %s como forma de pagamento. As formas aceitas são ",formaPagamento);
            for(int i=0; i<formasAceitas.size()-1;i++){
                mensagemErro = mensagemErro.concat(String.format("%s, ",formasAceitas.get(i).getNomeForma()));
            }
            mensagemErro = mensagemErro.concat(String.format("%s.",formasAceitas.get(formasAceitas.size()-1).getNomeForma()));
            throw new FormaPagamentoInvalidaException(mensagemErro);
        }
    }


    public void cancelarReserva(Long idReserva) throws ReservaNaoEncontradaException, ImpossivelCancelarException {

        Reserva reserva = buscarReservaPorId(idReserva);

        if(reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)) {
            reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
            reserva.setStatusReserva();
            reservaRepository.save(reserva);
        } else {
            throw new ImpossivelCancelarException();
        }

    }

    public void estornarReserva(Long idReserva) throws ReservaNaoEncontradaException, ImpossivelEstornarException {

        Reserva reserva = buscarReservaPorId(idReserva);

        if(reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO)) {
            reserva.getPagamento().setStatus(StatusPagamento.ESTORNADO);
            reserva.getPagamento().setFormaEscolhida(null);
            reserva.setStatusReserva();
            reservaRepository.save(reserva);
        } else {
            throw new ImpossivelEstornarException();
        }
    }
}
