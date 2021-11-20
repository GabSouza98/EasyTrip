package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.generica.TipoDominioNaoEncontradoException;
import io.github.cwireset.tcc.exception.reserva.*;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.repository.ReservaRepository;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.request.CadastrarReservaRequest;
import io.github.cwireset.tcc.response.DadosAnuncioResponse;
import io.github.cwireset.tcc.response.DadosSolicitanteResponse;
import io.github.cwireset.tcc.response.InformacaoReservaResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    ImovelService imovelService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ImovelRepository imovelRepository;

    @Mock
    UsuarioService usuarioService;

    @Mock
    AnuncioService anuncioService;

    @InjectMocks
    ReservaService reservaService;

    @Captor
    ArgumentCaptor<Reserva> reservaArgumentCaptor;

    Pageable page = PageRequest.of(0,10);

    //Arrange global
    Endereco endereco1 = new Endereco(1L,
            "99999-999",
            "Rua fulano de tal",
            "102",
            "Esquina da rua dos bobos",
            "Centro",
            "Pelotas",
            "SC");

    List<CaracteristicaImovel> listaCaracteristicas = new ArrayList<>();

    Usuario usuario = Usuario.builder()
            .id(1L)
            .nome("Abacaxi")
            .cpf("04283135070")
            .email("emailteste@gmail.com")
            .senha("super_secreto")
            .dataNascimento(LocalDate.of(1980, Month.JANUARY,1))
            .endereco(endereco1)
            .build();

    Imovel imovel = Imovel.builder()
            .id(1L)
            .identificacao("Casa grande perto da praia")
            .tipoImovel(TipoImovel.CASA)
            .endereco(endereco1)
            .proprietario(usuario)
            .caracteristicas(listaCaracteristicas)
            .ativo(true)
            .build();

    List<FormaPagamento> formasAceitas;

    Anuncio anuncio = Anuncio.builder()
            .id(1L)
            .tipoAnuncio(TipoAnuncio.COMPLETO)
            .imovel(imovel)
            .anunciante(usuario)
            .valorDiaria(BigDecimal.valueOf(100))
            .formasAceitas(formasAceitas)
            .descricao("blablabla")
            .ativo(true)
            .build();

    Periodo periodo = new Periodo(
            LocalDateTime.of(1998,Month.JANUARY,25,14,0),
            LocalDateTime.of(1998,Month.JANUARY,30,12,0));

    CadastrarReservaRequest reservaRequest = new CadastrarReservaRequest(
            2L,1L,periodo,5);

    Pagamento pagamento = new Pagamento(BigDecimal.valueOf(500L),null,StatusPagamento.PENDENTE);

    Reserva reserva = new Reserva(1L, usuario,anuncio,periodo,5,LocalDateTime.now(),pagamento);

    InformacaoReservaResponse informacaoReservaResponse = new InformacaoReservaResponse(
            null,
            new DadosSolicitanteResponse(usuario.getId(), usuario.getNome()),
            reservaRequest.getQuantidadePessoas(),
            new DadosAnuncioResponse(anuncio.getId(),
                    anuncio.getImovel(),
                    anuncio.getAnunciante(),
                    anuncio.getFormasAceitas(),
                    anuncio.getDescricao()),
            periodo,
            pagamento);


    @Test
    void testBuscarReservaPorId_ReservaNaoEncontrado() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Reserva com Id com o valor '1' foi encontrado.";
        //Act

        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                reservaService.buscarReservaPorId(reserva.getId()));
        assertEquals(expected, exception.getMessage());
    }



    @Test
    void testModificaPeriodoParaPadrao() throws Exception {
        //Arrange
        Periodo periodoForaDoPadrao = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,18,57),
                LocalDateTime.of(1998,Month.JANUARY,25,3,41)
                );
        Periodo padronizado = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                LocalDateTime.of(1998,Month.JANUARY,25,12,0)
        );
        //Act
        Periodo periodoModificado = reservaService.modificaHoraPeriodo(periodoForaDoPadrao);
        //Assert
        assertEquals(padronizado, periodoModificado);
    }

    @Test
    void testCalcularValorDaDiaria() throws Exception {
        //Arrange
        Periodo padronizado = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                LocalDateTime.of(1998,Month.JANUARY,30,12,0)
        );
        BigDecimal valorDiaria = new BigDecimal(100);
        BigDecimal valorEsperado = new BigDecimal(500);
        //Act
        BigDecimal valorCalculado = reservaService.calculaValorTotal(padronizado,valorDiaria);
        //Assert
        assertEquals(valorEsperado, valorCalculado);
    }

    @Test
    void testCadastrarReserva_DataInicialMaiorQueFinal() throws Exception {
        //Arrange
        Periodo dataInicialMaiorQueFinal = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,30,14,0),
                LocalDateTime.of(1998,Month.JANUARY,25,12,0)
        );
        reservaRequest.setPeriodo(dataInicialMaiorQueFinal);
        String expected = "Período inválido! A data final da reserva precisa ser maior do que a data inicial.";

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);

        //Assert
        DataSaidaMenorQueDataEntradaException exception = assertThrows(DataSaidaMenorQueDataEntradaException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_MenosQueUmaDiaria() throws Exception {
        //Arrange
        Periodo menosQueUmaDiaria = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                LocalDateTime.of(1998,Month.JANUARY,25,16,0)
        );
        reservaRequest.setPeriodo(menosQueUmaDiaria);
        String expected = "Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.";

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);

        //Assert
        NumeroMinimoDiariasException exception = assertThrows(NumeroMinimoDiariasException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_SolicitanteIdIgualAnuncianteId() throws Exception {
        //Arrange
        reservaRequest.setIdSolicitante(1L);
        String expected = "O solicitante de uma reserva não pode ser o próprio anunciante.";

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);

        //Assert
        SolicitanteIgualAnuncianteException exception = assertThrows(SolicitanteIgualAnuncianteException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_ReservaAtivaMesmoPeriodo() throws Exception {
        //Arrange
        String expected = "Este anuncio já esta reservado para o período informado.";
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);
        when(reservaRepository.findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                any(Long.class),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(reservas);
        //Assert
        ConflitoAnuncioException exception = assertThrows(ConflitoAnuncioException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_QuantidadePessoasInsuficiente() throws Exception {
        //Arrange
        anuncio.getImovel().setTipoImovel(TipoImovel.HOTEL);
        reservaRequest.setQuantidadePessoas(1);
        String expected = "Não é possivel realizar uma reserva com menos de 2 pessoas para imóveis do tipo Hotel";
        List<Reserva> reservas = new ArrayList<>();

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);
        when(reservaRepository.findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                any(Long.class),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(reservas);
        //Assert
        QuantidadePessoasInsuficienteException exception = assertThrows(QuantidadePessoasInsuficienteException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_QuantidadeDiariasInsuficiente() throws Exception {
        //Arrange
        anuncio.getImovel().setTipoImovel(TipoImovel.POUSADA);
        reservaRequest.setPeriodo(new Periodo(LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                                    LocalDateTime.of(1998,Month.JANUARY,29,16,0)));
        String expected = "Não é possivel realizar uma reserva com menos de 5 diárias para imóveis do tipo Pousada";
        List<Reserva> reservas = new ArrayList<>();

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);
        when(reservaRepository.findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                any(Long.class),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(reservas);
        //Assert
        QuantidadeDiasInsuficienteException exception = assertThrows(QuantidadeDiasInsuficienteException.class, () ->
                reservaService.cadastrarReserva(reservaRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCadastrarReserva_Sucesso() throws Exception {
        //Arrange
        reserva.setId(null);
        List<Reserva> reservas = new ArrayList<>();

        when(usuarioService.buscarUsuarioPorId(reservaRequest.getIdSolicitante())).thenReturn(usuario);
        when(anuncioService.buscarAnuncioPorId(reservaRequest.getIdAnuncio())).thenReturn(anuncio);
        when(reservaRepository.findByAnuncioIdAndStatusReservaTrueAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                any(Long.class),any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(reservas);

        //Act
        InformacaoReservaResponse informacaoSalva = reservaService.cadastrarReserva(reservaRequest);
        //Assert
        verify(reservaRepository).save(reservaArgumentCaptor.capture());
        Reserva reservaSalva = reservaArgumentCaptor.getValue();
        assertNull(reservaSalva.getPagamento().getFormaEscolhida());
        assertTrue(reservaSalva.getStatusReserva());
        assertEquals(reserva, reservaSalva);
        assertEquals(informacaoReservaResponse, informacaoSalva);
    }

    @Test
    void testPagarReserva_FormaPagamentoNaoAceita() throws Exception {
        //Arrange
        FormaPagamento formaPagamento = FormaPagamento.PIX;
        List<FormaPagamento> formasAceitas = new ArrayList<>();
        formasAceitas.add(FormaPagamento.CARTAO_CREDITO);
        formasAceitas.add(FormaPagamento.CARTAO_DEBITO);
        formasAceitas.add(FormaPagamento.DINHEIRO);
        reserva.getAnuncio().setFormasAceitas(formasAceitas);
        String expected = "O anúncio não aceita PIX como forma de pagamento. As formas aceitas são CARTAO_CREDITO, CARTAO_DEBITO, DINHEIRO.";

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Assert
        FormaPagamentoInvalidaException exception = assertThrows(FormaPagamentoInvalidaException.class, () ->
                reservaService.pagarReserva(1L,formaPagamento));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testPagarReserva_StatusPagamentoNaoPendente() throws Exception {
        //Arrange
        FormaPagamento formaPagamento = FormaPagamento.PIX;
        List<FormaPagamento> formasAceitas = new ArrayList<>();
        formasAceitas.add(FormaPagamento.PIX);
        reserva.getAnuncio().setFormasAceitas(formasAceitas);
        reserva.getPagamento().setStatus(StatusPagamento.PAGO);
        String expected = "Não é possível realizar o pagamento para esta reserva, pois ela não está no status PENDENTE.";

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Assert
        ImpossivelRealizarOperacaoException exception = assertThrows(ImpossivelRealizarOperacaoException.class, () ->
                reservaService.pagarReserva(1L,formaPagamento));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testPagarReserva_DevePagar() throws Exception {
        //Arrange
        FormaPagamento formaPagamento = FormaPagamento.PIX;
        List<FormaPagamento> formasAceitas = new ArrayList<>();
        formasAceitas.add(FormaPagamento.PIX);
        reserva.getAnuncio().setFormasAceitas(formasAceitas);
        reserva.getPagamento().setStatus(StatusPagamento.PENDENTE);
        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Act
        reservaService.pagarReserva(1L,formaPagamento);

        //Assert
        verify(reservaRepository).save(reservaArgumentCaptor.capture());
        Reserva reservaSalva = reservaArgumentCaptor.getValue();
        assertEquals(StatusPagamento.PAGO, reservaSalva.getPagamento().getStatus());
        assertEquals(formaPagamento, reservaSalva.getPagamento().getFormaEscolhida());
        assertTrue(reservaSalva.getStatusReserva());
    }

    @Test
    void testCancelarReserva_StatusPagamentoNaoPendente() throws Exception {
        //Arrange
        reserva.getPagamento().setStatus(StatusPagamento.PAGO);
        String expected = "Não é possível realizar o cancelamento para esta reserva, pois ela não está no status PENDENTE.";

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Assert
        ImpossivelRealizarOperacaoException exception = assertThrows(ImpossivelRealizarOperacaoException.class, () ->
                reservaService.cancelarReserva(1L));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testCancelarReserva_DeveCancelar() throws Exception {
        //Arrange
        reserva.getPagamento().setStatus(StatusPagamento.PENDENTE);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Act
        reservaService.cancelarReserva(1L);
        //Assert
        verify(reservaRepository).save(reservaArgumentCaptor.capture());
        Reserva reservaSalva = reservaArgumentCaptor.getValue();
        assertEquals(StatusPagamento.CANCELADO, reservaSalva.getPagamento().getStatus());
        assertFalse(reservaSalva.getStatusReserva());
    }

    @Test
    void testEstornarReserva_StatusPagamentoNaoPago() throws Exception {
        //Arrange
        reserva.getPagamento().setStatus(StatusPagamento.PENDENTE);
        String expected = "Não é possível realizar o estorno para esta reserva, pois ela não está no status PAGO.";

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Assert
        ImpossivelRealizarOperacaoException exception = assertThrows(ImpossivelRealizarOperacaoException.class, () ->
                reservaService.estornarReserva(1L));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testEstornarReserva_DeveEstornar() throws Exception {
        //Arrange
        reserva.getPagamento().setStatus(StatusPagamento.PAGO);

        when(reservaRepository.findById(anyLong())).thenReturn(Optional.of(reserva));
        //Act
        reservaService.estornarReserva(1L);
        //Assert
        verify(reservaRepository).save(reservaArgumentCaptor.capture());
        Reserva reservaSalva = reservaArgumentCaptor.getValue();
        assertEquals(StatusPagamento.ESTORNADO, reservaSalva.getPagamento().getStatus());
        assertFalse(reservaSalva.getStatusReserva());
    }

    @Test
    void testConsultarReservaPorSolicitante_PeriodoNulo() throws Exception {
        //Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);
        when(reservaRepository.findBySolicitanteId(1L, page)).thenReturn(new PageImpl(reservas, page, 2));

        //Act
        reservaService.consultarReservasPorSolicitante(1L, null, page);

        //Assert
        verify(reservaRepository, times(1)).findBySolicitanteId(1L, page);
        assertTrue(reservaService.consultarReservasPorSolicitante(1L,null,page).hasContent());
    }

    @Test
    void testConsultarReservaPorSolicitante_PeriodoParcialNulo() throws Exception {
        //Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);
        when(reservaRepository.findBySolicitanteId(1L, page)).thenReturn(new PageImpl(reservas, page, 2));
        Periodo periodoParcialNulo = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                null
        );
        //Act
        reservaService.consultarReservasPorSolicitante(1L, periodoParcialNulo, page);

        //Assert
        verify(reservaRepository, times(1)).findBySolicitanteId(1L, page);
        assertTrue(reservaService.consultarReservasPorSolicitante(1L,periodoParcialNulo,page).hasContent());
    }

    @Test
    void testConsultarReservaPorSolicitante_PeriodoCompleto() throws Exception {
        //Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);
        Periodo periodoCompleto = new Periodo(
                LocalDateTime.of(1998,Month.JANUARY,25,14,0),
                LocalDateTime.of(1998,Month.JANUARY,30,14,0)
        );
        when(reservaRepository.findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(1L,periodoCompleto.getDataHoraInicial(),periodoCompleto.getDataHoraFinal(), page)).thenReturn(new PageImpl(reservas, page, 2));

        //Act
        reservaService.consultarReservasPorSolicitante(1L, periodoCompleto, page);

        //Assert
        verify(reservaRepository, times(1)).findBySolicitanteIdAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(1L, periodoCompleto.getDataHoraInicial(), periodoCompleto.getDataHoraFinal(), page);
        assertTrue(reservaService.consultarReservasPorSolicitante(1L,periodoCompleto,page).hasContent());
    }

    @Test
    void testConsultarReservaPorAnunciante() throws Exception {
        //Arrange
        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);
        when(reservaRepository.findByAnuncioAnuncianteId(1L, page)).thenReturn(new PageImpl(reservas, page, 2));

        //Act
        reservaService.consultarReservasPorAnunciante(1L, page);

        //Assert
        verify(reservaRepository, times(1)).findByAnuncioAnuncianteId(1L, page);
        assertTrue(reservaService.consultarReservasPorAnunciante(1L,page).hasContent());
    }




}
