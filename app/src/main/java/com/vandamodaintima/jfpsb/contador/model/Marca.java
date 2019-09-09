package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;

public class Marca implements Serializable {
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id"};
    }

    public static String[] getHeaders() {
        return new String[]{"Nome"};
    }
}
