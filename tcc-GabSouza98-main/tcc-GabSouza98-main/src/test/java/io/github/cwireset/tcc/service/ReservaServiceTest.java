package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.repository.ImovelRepository;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

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
}
