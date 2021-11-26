package io.github.cwireset.tcc.domain;

import lombok.Getter;

@Getter
public enum TipoDominio {

    USUARIO("Usuario"),
    IMOVEL("Imovel"),
    ANUNCIO("Anuncio"),
    RESERVA("Reserva");

    private String nomeDominio;

    TipoDominio(String nomeDominio) {
        this.nomeDominio = nomeDominio;
    }
}
