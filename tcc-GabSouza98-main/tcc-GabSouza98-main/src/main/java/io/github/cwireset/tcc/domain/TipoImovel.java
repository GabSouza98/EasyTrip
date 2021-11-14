package io.github.cwireset.tcc.domain;

public enum TipoImovel {

    APARTAMENTO("Apartamento"),
    CASA("Casa"),
    HOTEL("Hotel"),
    POUSADA("Pousada");

    private String nome;

    TipoImovel(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
