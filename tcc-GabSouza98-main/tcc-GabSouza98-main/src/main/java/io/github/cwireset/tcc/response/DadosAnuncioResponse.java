package io.github.cwireset.tcc.response;

import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DadosAnuncioResponse {

    private Long id;
    private Imovel imovel;
    private Usuario anunciante;
    private List<FormaPagamento> formasAceitas;
    private String descricao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DadosAnuncioResponse that = (DadosAnuncioResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(imovel, that.imovel) && Objects.equals(anunciante, that.anunciante) && Objects.equals(formasAceitas, that.formasAceitas) && Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imovel, anunciante, formasAceitas, descricao);
    }
}
