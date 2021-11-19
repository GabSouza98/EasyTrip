package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.CaracteristicaImovel;
import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.TipoImovel;
import io.github.cwireset.tcc.domain.Usuario;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarImovelRequest {

    @NotBlank
    private String identificacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoImovel tipoImovel;

    @NotNull
    @Valid
    private Endereco endereco;

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Long idProprietario;

    private List<CaracteristicaImovel> caracteristicas;

}
