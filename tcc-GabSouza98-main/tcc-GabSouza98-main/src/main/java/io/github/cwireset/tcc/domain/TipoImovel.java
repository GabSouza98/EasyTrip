package io.github.cwireset.tcc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TipoImovel {

    APARTAMENTO("Apartamento",0,0),
    CASA("Casa",0,0),
    HOTEL("Hotel",2,0),
    POUSADA("Pousada",0,5);

    private String nome;
    private Integer pessoasMinimo;
    private Integer diariasMinimo;

    TipoImovel(String nome, Integer pessoasMinimo, Integer diariasMinimo) {
        this.nome = nome;
        this.pessoasMinimo = pessoasMinimo;
        this.diariasMinimo = diariasMinimo;
    }
}
