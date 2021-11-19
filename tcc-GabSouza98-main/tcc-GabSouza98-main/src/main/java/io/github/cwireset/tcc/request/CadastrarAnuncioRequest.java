package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.TipoAnuncio;
import io.github.cwireset.tcc.domain.Usuario;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CadastrarAnuncioRequest {

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Long idImovel;

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private Long idAnunciante;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoAnuncio tipoAnuncio;

    @NotNull
    @Digits(integer = 100, fraction = 0)
    private BigDecimal valorDiaria;

    @NotNull
    @NotEmpty
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FormaPagamento> formasAceitas;

    @NotBlank
    private String descricao;


}
