package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OperadoraCartao implements IModel {
    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "IdentificadoresBanco")
    private List<String> identificadoresBanco = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getIdentificadoresBanco() {
        return identificadoresBanco;
    }

    public void setIdentificadoresBanco(List<String> identificadoresBanco) {
        this.identificadoresBanco = identificadoresBanco;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id"};
    }

    @Override
    public Object getIdentifier() {
        return nome;
    }

    @Override
    public String getDatabaseLogIdentifier() {
        return nome;
    }

    @Override
    public String getDeleteWhereClause() {
        return "nome = ?";
    }
}
