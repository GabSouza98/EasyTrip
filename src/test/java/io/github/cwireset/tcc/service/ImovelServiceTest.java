package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.generica.TipoDominioNaoEncontradoException;
import io.github.cwireset.tcc.exception.imovel.ImovelAtreladoAnuncioAtivoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.externalAPI.ClientFeign;
import io.github.cwireset.tcc.externalAPI.PostDTO;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImovelServiceTest {

    @Mock
    ImovelRepository imovelRepository;

    @Mock
    AnuncioService anuncioService;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    ImovelService imovelService;

    @Captor
    ArgumentCaptor<Imovel> imovelArgumentCaptor;

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

    CadastrarImovelRequest cadastrarImovelRequest = CadastrarImovelRequest.builder()
            .identificacao("Casa grande perto da praia")
            .tipoImovel(TipoImovel.CASA)
            .endereco(endereco1)
            .idProprietario(1L)
            .caracteristicas(listaCaracteristicas)
            .build();

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



    @Test
    public void testCadastrarImovelComSucesso() throws Exception {

        //Act
        when(usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario())).thenReturn(usuario);
        Imovel imovelSalvo = imovelService.cadastrarImovel(cadastrarImovelRequest);

        //Assert
        Mockito.verify(imovelRepository, Mockito.times(1)).save(imovelSalvo);
        Mockito.verify(imovelRepository).save(imovelArgumentCaptor.capture());
        Imovel imovelCadastrado = imovelArgumentCaptor.getValue();
        assertEquals(imovelCadastrado.getProprietario(), usuario);
        assertNull(imovelCadastrado.getId());
        assertTrue(imovelCadastrado.getAtivo());
    }

    @Test
    public void testCadastrarImovel_ProprietarioInvalido() throws Exception {

        //Arrange
        String expected = "Nenhum(a) Usuario com Id com o valor '1' foi encontrado.";

        //Act
        when(usuarioService.buscarUsuarioPorId(cadastrarImovelRequest.getIdProprietario())).thenReturn(null);

        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                imovelService.cadastrarImovel(cadastrarImovelRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testDeveListarImoveis_ListaPreenchida() throws Exception {
        //Arrange
        List<Imovel> imoveis = new ArrayList<>();
        imoveis.add(imovel);
        //Act
        when(imovelRepository.findByAtivoIsTrue(page)).thenReturn(new PageImpl(imoveis, page, 2));
        //Assert
        assertTrue(imovelService.listarImoveis(page).hasContent());
        assertTrue(imovelService.listarImoveis(page).getContent().contains(imovel));
    }

    @Test
    void testDeveListarImoveis_ListaVazia() throws Exception {
        //Arrange
        List<Imovel> imoveis = new ArrayList<>();
        //Act
        when(imovelRepository.findByAtivoIsTrue(page)).thenReturn(new PageImpl(imoveis, page, 2));
        //Assert
        assertFalse(imovelService.listarImoveis(page).hasContent());
    }

    @Test
    void testDeveListarImoveis_PorProprietario_ListaPreenchida() throws Exception {
        //Arrange
        List<Imovel> imoveis = new ArrayList<>();
        imoveis.add(imovel);
        //Act
        when(imovelRepository.findByProprietarioIdAndAtivoIsTrue(imovel.getProprietario().getId(),page)).thenReturn(new PageImpl(imoveis, page, 2));
        //Assert
        assertTrue(imovelService.listarImoveisPorIdProprietario(imovel.getProprietario().getId(),page).hasContent());
        assertTrue(imovelService.listarImoveisPorIdProprietario(imovel.getProprietario().getId(),page).getContent().contains(imovel));
    }

    @Test
    void testDeveListarImoveis_PorProprietario_ListaVazia() throws Exception {
        //Arrange
        List<Imovel> imoveis = new ArrayList<>();
        //Act
        when(imovelRepository.findByProprietarioIdAndAtivoIsTrue(imovel.getProprietario().getId(),page)).thenReturn(new PageImpl(imoveis, page, 2));
        //Assert
        assertFalse(imovelService.listarImoveisPorIdProprietario(imovel.getProprietario().getId(),page).hasContent());
    }

    @Test
    void testDeveBuscarImovelPorId_DeveRetornarImovel() throws Exception {
        //Act
        when(imovelRepository.findByIdAndAtivoIsTrue(imovel.getId())).thenReturn(Optional.of(imovel));
        Imovel imovelEsperado = imovelService.buscarImovelPorId(imovel.getId());
        //Assert
        Mockito.verify(imovelRepository, times(1)).findByIdAndAtivoIsTrue(imovel.getId());
        Assertions.assertEquals(imovel, imovelEsperado);
    }

    @Test
    void testDeveBuscarImovelPorId_NaoDeveRetornarImovel() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Imovel com Id com o valor '1' foi encontrado.";
        //Act
        when(imovelRepository.findByIdAndAtivoIsTrue(imovel.getId())).thenReturn(Optional.empty());
        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                imovelService.buscarImovelPorId(imovel.getId()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testExcluirImovel_ImovelNaoEncontrado() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Imovel com Id com o valor '1' foi encontrado.";
        //Act

        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                imovelService.excluirImovelPorId(imovel.getId()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testExcluirImovel_ImovelAtreladoAnuncio() throws Exception {
        //Arrange
        String expected = "Não é possível excluir um imóvel que possua um anúncio.";
        //Act
        when(imovelRepository.findByIdAndAtivoIsTrue(imovel.getId())).thenReturn(Optional.of(imovel));
        when(anuncioService.buscarPorImovelId(imovel.getId())).thenReturn(true);
        //Assert
        ImovelAtreladoAnuncioAtivoException exception = assertThrows(ImovelAtreladoAnuncioAtivoException.class, () ->
                imovelService.excluirImovelPorId(imovel.getId()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testExcluirImovel_DeveExcluirImovel() throws Exception {
        //Arrange
        when(imovelRepository.findByIdAndAtivoIsTrue(imovel.getId())).thenReturn(Optional.of(imovel));
        when(anuncioService.buscarPorImovelId(imovel.getId())).thenReturn(false);
        //Act
        imovelService.excluirImovelPorId(imovel.getId());
        //Assert
        verify(imovelRepository).save(imovelArgumentCaptor.capture());

        imovel.setAtivo(false);
        assertEquals(imovel, imovelArgumentCaptor.getValue());
        assertFalse(imovelArgumentCaptor.getValue().getAtivo());
    }



}
