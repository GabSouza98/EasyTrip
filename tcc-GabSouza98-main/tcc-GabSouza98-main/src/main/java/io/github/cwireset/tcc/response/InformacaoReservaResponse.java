package io.github.cwireset.tcc.response;

import io.github.cwireset.tcc.domain.Pagamento;
import io.github.cwireset.tcc.domain.Periodo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InformacaoReservaResponse {

    private Long idReserva;
    private DadosSolicitanteResponse solicitante;
    private Integer quantidadePessoas;
    private DadosAnuncioResponse anuncio;
    private Periodo periodo;
    private Pagamento pagamento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformacaoReservaResponse that = (InformacaoReservaResponse) o;
        return Objects.equals(idReserva, that.idReserva) && Objects.equals(solicitante, that.solicitante) && Objects.equals(quantidadePessoas, that.quantidadePessoas) && Objects.equals(anuncio, that.anuncio) && Objects.equals(periodo, that.periodo) && Objects.equals(pagamento, that.pagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva, solicitante, quantidadePessoas, anuncio, periodo, pagamento);
    }
}
