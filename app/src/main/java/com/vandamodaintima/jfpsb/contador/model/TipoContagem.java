package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TipoContagem implements Serializable, IModel {
    @SerializedName(value = "Id")
    private int id;
    @SerializedName(value = "Nome")
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
    public Object getIdentifier() {
        return id;
    }

    @Override
    public String getDatabaseLogIdentifier() {
        return String.valueOf(id);
    }

    @Override
    public String getDeleteWhereClause() {
        return "id = ?";
    }
}
