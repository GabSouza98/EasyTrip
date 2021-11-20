package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.anuncio.AnuncioDuplicadoException;
import io.github.cwireset.tcc.exception.anuncio.AnuncioNaoEncontradoException;
import io.github.cwireset.tcc.exception.generica.TipoDominioNaoEncontradoException;
import io.github.cwireset.tcc.exception.imovel.ImovelNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import io.github.cwireset.tcc.request.CadastrarImovelRequest;
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
public class AnuncioServiceTest {

    @Mock
    AnuncioRepository anuncioRepository;

    @Mock
    ImovelService imovelService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ImovelRepository imovelRepository;

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    AnuncioService anuncioService;

    @Captor
    ArgumentCaptor<Anuncio> anuncioArgumentCaptor;

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
            .valorDiaria(BigDecimal.valueOf(1000))
            .formasAceitas(formasAceitas)
            .descricao("blablabla")
            .ativo(true)
            .build();

    CadastrarAnuncioRequest anuncioRequest = CadastrarAnuncioRequest.builder()
            .idImovel(1L)
            .idAnunciante(1L)
            .tipoAnuncio(TipoAnuncio.COMPLETO)
            .valorDiaria(BigDecimal.valueOf(100L))
            .formasAceitas(formasAceitas)
            .descricao("Casa na praia")
            .build();

    @Test
    public void testCadastrarAnuncioComSucesso() throws Exception {

        //Act
        when(imovelService.buscarImovelPorId(anuncioRequest.getIdImovel())).thenReturn(imovel);
        when(usuarioService.buscarUsuarioPorId(anuncioRequest.getIdAnunciante())).thenReturn(usuario);

        Anuncio anuncioSalvo = anuncioService.cadastrarAnuncio(anuncioRequest);

        //Assert
        Mockito.verify(anuncioRepository, Mockito.times(1)).save(anuncioSalvo);
        Mockito.verify(anuncioRepository).save(anuncioArgumentCaptor.capture());
        Anuncio anuncioCadastrado = anuncioArgumentCaptor.getValue();
        assertEquals(anuncioCadastrado.getAnunciante(), anuncioSalvo.getAnunciante());
        assertEquals(anuncioCadastrado.getAnunciante(), usuario);
        assertEquals(anuncioCadastrado.getImovel(), imovel);
        assertNull(anuncioCadastrado.getId());
        assertTrue(anuncioCadastrado.getAtivo());
    }

    @Test
    public void testCadastrarAnuncio_AnuncianteInvalido() throws Exception {

        //Arrange
        String expected = "Nenhum(a) Usuario com Id com o valor '1' foi encontrado.";

        //Act
        when(imovelService.buscarImovelPorId(anuncioRequest.getIdImovel())).thenReturn(imovel);
        when(usuarioService.buscarUsuarioPorId(anuncioRequest.getIdAnunciante())).thenReturn(null);

        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                anuncioService.cadastrarAnuncio(anuncioRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testCadastrarAnuncio_ImovelInvalido() throws Exception {

        //Arrange
        String expected = "Nenhum(a) Imovel com Id com o valor '1' foi encontrado.";
        when(imovelService.buscarImovelPorId(anuncioRequest.getIdImovel())).thenReturn(null);

        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                anuncioService.cadastrarAnuncio(anuncioRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void testCadastrarAnuncio_AnuncioCadastradoMesmoImovel() throws Exception {

        //Arrange
        String expected = "Já existe um recurso do tipo Anuncio com IdImovel com o valor '1'.";
        when(imovelService.buscarImovelPorId(anuncioRequest.getIdImovel())).thenReturn(imovel);
        when(usuarioService.buscarUsuarioPorId(anuncioRequest.getIdAnunciante())).thenReturn(usuario);
        when(anuncioRepository.findByImovelIdAndImovelAtivoIsTrue(anuncioRequest.getIdImovel())).thenReturn(anuncio);

        //Assert
        AnuncioDuplicadoException exception = assertThrows(AnuncioDuplicadoException.class, () ->
                anuncioService.cadastrarAnuncio(anuncioRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testDeveListarAnuncio_ListaPreenchida() throws Exception {
        //Arrange
        List<Anuncio> anuncios = new ArrayList<>();
        anuncios.add(anuncio);
        //Act
        when(anuncioRepository.findByAtivoIsTrue(page)).thenReturn(new PageImpl(anuncios, page, 2));
        //Assert
        assertTrue(anuncioService.listarAnuncios(page).hasContent());
        assertTrue(anuncioService.listarAnuncios(page).getContent().contains(anuncio));
    }

    @Test
    void testDeveListarAnuncio_ListaVazia() throws Exception {
        //Arrange
        List<Anuncio> anuncios = new ArrayList<>();
        //Act
        when(anuncioRepository.findByAtivoIsTrue(page)).thenReturn(new PageImpl(anuncios, page, 7));
        //Assert
        assertFalse(anuncioService.listarAnuncios(page).hasContent());
        assertEquals(1,anuncioService.listarAnuncios(page).getTotalPages());
    }

    @Test
    void testDeveListarAnuncio_PorAnunciante_ListaPreenchida() throws Exception {
        //Arrange
        List<Anuncio> anuncios = new ArrayList<>();
        anuncios.add(anuncio);
        //Act
        when(anuncioRepository.findByAnuncianteIdAndAtivoIsTrue(anuncio.getAnunciante().getId(),page)).thenReturn(new PageImpl(anuncios, page, 2));
        //Assert
        assertTrue(anuncioService.listarAnunciosPorIdAnunciante(anuncio.getAnunciante().getId(),page).hasContent());
        assertTrue(anuncioService.listarAnunciosPorIdAnunciante(anuncio.getAnunciante().getId(),page).getContent().contains(anuncio));
    }

    @Test
    void testDeveListarAnuncio_PorAnunciante_ListaVazia() throws Exception {
        //Arrange
        List<Anuncio> anuncios = new ArrayList<>();
        //Act
        when(anuncioRepository.findByAnuncianteIdAndAtivoIsTrue(anuncio.getAnunciante().getId(),page)).thenReturn(new PageImpl(anuncios, page, 2));
        //Assert
        assertFalse(anuncioService.listarAnunciosPorIdAnunciante(anuncio.getAnunciante().getId(),page).hasContent());
        assertEquals(1,anuncioService.listarAnunciosPorIdAnunciante(anuncio.getAnunciante().getId(),page).getTotalPages());
    }

    @Test
    void testExcluirAnuncio_AnuncioNaoEncontrado() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Anuncio com Id com o valor '1' foi encontrado.";
        //Act
        when(anuncioRepository.findByIdAndAtivoIsTrue(anuncio.getId())).thenReturn(Optional.empty());
        //Assert
        TipoDominioNaoEncontradoException exception = assertThrows(TipoDominioNaoEncontradoException.class, () ->
                anuncioService.excluirAnuncioPorId(anuncio.getId()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testExcluirAnuncio_DeveExcluirAnuncioLogicamente() throws Exception {
        //Arrange
        when(anuncioRepository.findByIdAndAtivoIsTrue(anuncio.getId())).thenReturn(Optional.of(anuncio));
        //Act
        anuncioService.excluirAnuncioPorId(anuncio.getId());
        //Assert
        verify(anuncioRepository).save(anuncioArgumentCaptor.capture());
        anuncio.setAtivo(false);
        assertEquals(anuncio, anuncioArgumentCaptor.getValue());
        assertFalse(anuncioArgumentCaptor.getValue().getAtivo());
    }

    @Test
    void testBuscarPorImovelId_DeveRetornarTrue() throws Exception {
        //Arrange
        when(anuncioRepository.findByImovelIdAndAtivoIsTrue(anyLong())).thenReturn(anuncio);
        //Act
        Boolean pesquisa = anuncioService.buscarPorImovelId(anuncio.getImovel().getId());
        //Assert
        assertTrue(pesquisa);
    }

    @Test
    void testBuscarPorImovelId_DeveRetornarFalse() throws Exception {
        //Arrange
        when(anuncioRepository.findByImovelIdAndAtivoIsTrue(anyLong())).thenReturn(null);
        //Act
        Boolean pesquisa = anuncioService.buscarPorImovelId(anuncio.getImovel().getId());
        //Assert
        assertFalse(pesquisa);
    }

}
