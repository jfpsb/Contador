package com.vandamodaintima.jfpsb.contador.model;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class TipoContagem implements Serializable, IModel {
    @Element(name = "Id")
    private int id;
    @Element(name = "Nome")
    private String nome;

    public TipoContagem() {
    }

    public TipoContagem(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String[] getColunas() {
        return new String[]{"id as _id", "nome"};
    }

    @Override
    public String getIdentificador() {
        return String.valueOf(id);
    }
}
