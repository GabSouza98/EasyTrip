package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.Endereco;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarUsuarioRequest {
    @NotBlank
    private String nome;
    @NotBlank
    @Email(message = "Email inválido")
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    private LocalDate dataNascimento;
    @Valid
    private Endereco endereco;
}
