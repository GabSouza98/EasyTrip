package io.github.cwireset.tcc.domain;

import lombok.Getter;

@Getter
public enum StatusPagamento {

    PENDENTE("","PENDENTE"),
    PAGO("pagamento","PAGO"),
    ESTORNADO("estorno","ESTORNADO"),
    CANCELADO("cancelamento","CANCELADO");

    private String operacao;
    private String nomeStatus;

    StatusPagamento(String operacao, String nomeStatus) {
        this.operacao = operacao;
        this.nomeStatus = nomeStatus;
    }


}
