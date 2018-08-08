package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

public class Marca implements Serializable {
    private String nome;

    private static final String[] colunas = new String[]{"nome as _id"};

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String[] getColunas() {
        return colunas;
    }
}
