package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Marca implements Serializable, IModel {
    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "Fornecedor")
    private Fornecedor fornecedor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id", "fornecedor"};
    }

    public static String[] getHeaders() {
        return new String[]{"Nome", "Fornecedor"};
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
