package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

public class Marca implements Serializable {
    private long id;
    private String nome;

    private static final String[] colunas = new String[]{"id as _id", "nome"};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
