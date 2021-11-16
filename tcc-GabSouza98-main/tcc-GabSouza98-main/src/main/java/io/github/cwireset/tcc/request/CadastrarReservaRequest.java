package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.Periodo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CadastrarReservaRequest {

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Long idSolicitante;

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Long idAnuncio;

    @NotNull
    @Valid
    @Embedded
    private Periodo periodo;

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Integer quantidadePessoas;
}
