package io.github.cwireset.tcc.domain;

public enum FormaPagamento {

    CARTAO_CREDITO("CARTAO_CREDITO"),
    CARTAO_DEBITO("CARTAO_DEBITO"),
    PIX("PIX"),
    DINHEIRO("DINHEIRO");

    private String nomeForma;

    FormaPagamento(String nomeForma) {
        this.nomeForma = nomeForma;
    }

    public String getNomeForma() {
        return nomeForma;
    }

    public void setNomeForma(String nomeForma) {
        this.nomeForma = nomeForma;
    }
}
