package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.usuario.CpfDuplicadoException;
import io.github.cwireset.tcc.exception.usuario.CpfNaoEncontradoException;
import io.github.cwireset.tcc.exception.usuario.EmailDuplicadoException;
import io.github.cwireset.tcc.exception.usuario.UsuarioNaoEncontradoException;
import io.github.cwireset.tcc.externalAPI.ClientFeign;
import io.github.cwireset.tcc.externalAPI.PostDTO;
import io.github.cwireset.tcc.repository.UsuarioRepository;
import io.github.cwireset.tcc.request.AtualizarUsuarioRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    ClientFeign clientFeign;

    @InjectMocks
    UsuarioService usuarioService;

    @Captor
    ArgumentCaptor<Usuario> usuarioArgumentCaptor;

    //Arrange que será usado para todos os testes
    Endereco endereco1 = new Endereco(1L,
            "99999-999",
            "Rua fulano de tal",
            "102",
            "Esquina da rua dos bobos",
            "Centro",
            "Pelotas",
            "SC");

    Endereco endereco2 = new Endereco(2L,
            "99999-999",
            "Rua fulano de tal",
            "102",
            "Esquina da rua dos bobos",
            "Centro",
            "Pelotas",
            "SC");

    Usuario usuario = Usuario.builder()
            .id(1L)
            .nome("Abacaxi")
            .cpf("04283135070")
            .email("emailteste@gmail.com")
            .senha("super_secreto")
            .dataNascimento(LocalDate.of(1980, Month.JANUARY,1))
            .endereco(endereco1)
            .build();

    Usuario usuario2 = Usuario.builder()
            .nome("Banana")
            .cpf("04283135070")
            .email("emailteste2@gmail.com")
            .senha("super_secreto")
            .dataNascimento(LocalDate.of(1980, Month.JANUARY,1))
            .endereco(endereco1)
            .build();

    AtualizarUsuarioRequest atualizarUsuarioRequest = AtualizarUsuarioRequest.builder()
            .nome("Josefino")
            .email("emailteste@gmail.com")
            .senha("nao_tao_secreto")
            .dataNascimento(LocalDate.of(1998,Month.JANUARY,30))
            .endereco(endereco1)
            .build();

    Pageable page = PageRequest.of(0,10);

    @Test
    public void testCadastrarUsuarioComSucesso() throws Exception {

        //Arrange
        PostDTO postDTO = new PostDTO("http://dogs.com.br");

        //Act
        when(clientFeign.buscarLink()).thenReturn(postDTO);
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(usuario);

        //Assert
        Mockito.verify(usuarioRepository, Mockito.times(1)).save(usuarioSalvo);
        Mockito.verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        Usuario usuarioCadastrado = usuarioArgumentCaptor.getValue();
        assertEquals(usuarioCadastrado.getNome(), usuario.getNome());
        assertNull(usuarioCadastrado.getId());
    }

    @Test
    void testNaoDeveCadastrarCpfDuplicado() throws Exception {
        //Arrange
        String expected = "Já existe um recurso do tipo Usuario com CPF com o valor '04283135070'.";

        //Act
        when(usuarioRepository.findByCpfEquals(usuario.getCpf())).thenReturn(usuario);

        //Assert
        CpfDuplicadoException exception = Assertions.assertThrows(CpfDuplicadoException.class,
                () -> usuarioService.cadastrarUsuario(usuario));
        assertEquals(exception.getMessage(), expected);
    }

    @Test
    void testNaoDeveCadastrarEmailDuplicado() throws Exception {
        //Arrange
        String expected = "Já existe um recurso do tipo Usuario com E-Mail com o valor 'emailteste@gmail.com'.";

        //Act
        when(usuarioRepository.findByEmailEquals(usuario.getEmail())).thenReturn(usuario);

        //Assert
        EmailDuplicadoException exception = Assertions.assertThrows(EmailDuplicadoException.class,
                () -> usuarioService.cadastrarUsuario(usuario));
        assertEquals(exception.getMessage(), expected);
    }

    @Test
    void testDeveListarUsuarios_ListaPreenchida() throws Exception {
        //Arrange
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario2);
        usuarios.add(usuario);

        //Act
        when(usuarioRepository.findAll(page)).thenReturn(new PageImpl(usuarios, page, 2));
        //Assert
        assertTrue(usuarioService.listarUsuarios(page).hasContent());
    }

    @Test
    void testDeveListarUsuarios_ListaVazia() throws Exception {
        //Arrange
        List<Usuario> usuarios = new ArrayList<>();

        //Act
        when(usuarioRepository.findAll(page)).thenReturn(new PageImpl(usuarios, page, 2));
        //Assert
        assertFalse(usuarioService.listarUsuarios(page).hasContent());
    }

    @Test
    void testBuscarUsuarioPorId_DeveRetornarUsuario() throws Exception {
        //Arrange
        Usuario expected = usuario;
        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        Usuario usuarioProcurado = usuarioService.buscarUsuarioPorId(usuario.getId());
        //Assert
        Mockito.verify(usuarioRepository, Mockito.times(1)).findById(usuario.getId());
        Assertions.assertEquals(expected, usuarioProcurado);
    }

    @Test
    void testBuscarUsuarioPorId_NaoDeveEncontrarUsuario() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Usuario com Id com o valor '1' foi encontrado.";
        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());
        //Assert
        UsuarioNaoEncontradoException exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioService.buscarUsuarioPorId(usuario.getId()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testBuscarUsuarioPorCpf_DeveRetornarUsuario() throws Exception {
        //Arrange
        Usuario expected = usuario;
        //Act
        when(usuarioRepository.findByCpfEquals(usuario.getCpf())).thenReturn(usuario);
        Usuario usuarioProcurado = usuarioService.buscarUsuarioPorCpf(usuario.getCpf());
        //Assert
        Mockito.verify(usuarioRepository, Mockito.times(1)).findByCpfEquals(usuario.getCpf());
        Assertions.assertEquals(expected, usuarioProcurado);
    }

    @Test
    void testBuscarUsuarioPorCpf_NaoDeveEncontrarUsuario() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Usuario com CPF com o valor '04283135070' foi encontrado.";
        //Act
        when(usuarioRepository.findByCpfEquals(usuario.getCpf())).thenReturn(null);
        //Assert
        CpfNaoEncontradoException exception = Assertions.assertThrows(CpfNaoEncontradoException.class,
                () -> usuarioService.buscarUsuarioPorCpf(usuario.getCpf()));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testAtualizarUsuario_NaoDeveEncontrarUsuario() throws Exception {
        //Arrange
        String expected = "Nenhum(a) Usuario com Id com o valor '1' foi encontrado.";
        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());
        //Assert
        UsuarioNaoEncontradoException exception = Assertions.assertThrows(UsuarioNaoEncontradoException.class,
                () -> usuarioService.atualizarUsuario(1L,atualizarUsuarioRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testAtualizarUsuario_AlteraParaEmailNovoValido() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("novoemail@gmail.com");
        usuario.setEmail("teste@gmail.com");

        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail())).thenReturn(null);

        //Assert
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(1L, atualizarUsuarioRequest);
        assertEquals(usuario.getEmail(), usuarioAtualizado.getEmail());
    }

    @Test
    void testAtualizarUsuario_AlteraParaEmailIgualAnterior() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("teste@gmail.com");
        usuario.setEmail("teste@gmail.com");

        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        //Assert
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(1L, atualizarUsuarioRequest);
        assertEquals(usuario, usuarioAtualizado);
    }

    @Test
    void testAtualizarUsuario_AlteraParaEmailNovoInvalido() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("emailrepetido@gmail.com");
        usuario.setEmail("teste@gmail.com");
        String expected = "Já existe um recurso do tipo Usuario com E-Mail com o valor 'emailrepetido@gmail.com'.";

        //Act
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail())).thenReturn(usuario);

        //Assert
        EmailDuplicadoException exception = Assertions.assertThrows(EmailDuplicadoException.class,
                () -> usuarioService.atualizarUsuario(1L,atualizarUsuarioRequest));
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testAtualizarUsuario_AlteraEnderecoExistenteParaNull() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("novoemail@gmail.com");
        usuario.setEmail("testeemail@gmail.com");

        usuario.setEndereco(endereco1);
        atualizarUsuarioRequest.setEndereco(null);

        Usuario usuarioAtualizado = Usuario.builder()
                .id(usuario.getId())
                .nome(atualizarUsuarioRequest.getNome())
                .email(atualizarUsuarioRequest.getEmail())
                .senha(atualizarUsuarioRequest.getSenha())
                .cpf(usuario.getCpf())
                .dataNascimento(atualizarUsuarioRequest.getDataNascimento())
                .imagemAvatar(usuario.getImagemAvatar())
                .endereco(atualizarUsuarioRequest.getEndereco())
                .build();

        //Act
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail())).thenReturn(null);
        usuarioService.atualizarUsuario(usuario.getId(),atualizarUsuarioRequest);

        //Assert
        verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();
        assertEquals(usuarioAtualizado.getId(),usuarioCapturado.getId());
        assertNull(usuarioCapturado.getEndereco());
    }

    @Test
    void testAtualizarUsuario_AlteraEnderecoExistenteParaEnderecoNovo() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("novoemail@gmail.com");
        usuario.setEmail("testeemail@gmail.com");

        usuario.setEndereco(endereco1);
        atualizarUsuarioRequest.setEndereco(endereco2);

        Usuario usuarioAtualizado = Usuario.builder()
                .id(usuario.getId())
                .nome(atualizarUsuarioRequest.getNome())
                .email(atualizarUsuarioRequest.getEmail())
                .senha(atualizarUsuarioRequest.getSenha())
                .cpf(usuario.getCpf())
                .dataNascimento(atualizarUsuarioRequest.getDataNascimento())
                .imagemAvatar(usuario.getImagemAvatar())
                .endereco(atualizarUsuarioRequest.getEndereco())
                .build();

        //Act
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail())).thenReturn(null);
        usuarioService.atualizarUsuario(usuario.getId(),atualizarUsuarioRequest);

        //Assert
        verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();
        assertEquals(usuarioAtualizado.getId(),usuarioCapturado.getId());
        assertEquals(usuarioAtualizado.getEndereco(),usuarioCapturado.getEndereco());
    }

    @Test
    void testAtualizarUsuario_AlteraEnderecoNullParaEnderecoNovo() throws Exception {
        //Arrange
        atualizarUsuarioRequest.setEmail("novoemail@gmail.com");
        usuario.setEmail("testeemail@gmail.com");

        usuario.setEndereco(null);
        atualizarUsuarioRequest.setEndereco(endereco2);

        Usuario usuarioAtualizado = Usuario.builder()
                .id(usuario.getId())
                .nome(atualizarUsuarioRequest.getNome())
                .email(atualizarUsuarioRequest.getEmail())
                .senha(atualizarUsuarioRequest.getSenha())
                .cpf(usuario.getCpf())
                .dataNascimento(atualizarUsuarioRequest.getDataNascimento())
                .imagemAvatar(usuario.getImagemAvatar())
                .endereco(atualizarUsuarioRequest.getEndereco())
                .build();

        //Act
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailEquals(atualizarUsuarioRequest.getEmail())).thenReturn(null);
        usuarioService.atualizarUsuario(usuario.getId(),atualizarUsuarioRequest);

        //Assert
        verify(usuarioRepository).save(usuarioArgumentCaptor.capture());
        Usuario usuarioCapturado = usuarioArgumentCaptor.getValue();
        assertEquals(usuarioAtualizado.getId(),usuarioCapturado.getId());
        assertEquals(usuarioAtualizado.getEndereco(),usuarioCapturado.getEndereco());
    }





}
