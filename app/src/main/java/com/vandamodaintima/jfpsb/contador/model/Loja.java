package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Loja implements Serializable, IModel {
    @SerializedName(value = "Cnpj")
    private String cnpj;
    @SerializedName(value = "Matriz")
    private Loja matriz;
    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "Telefone")
    private String telefone;
    @SerializedName(value = "Endereco")
    private String endereco;
    @SerializedName(value = "InscricaoEstadual")
    private String inscricaoEstadual;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Loja getMatriz() {
        return matriz;
    }

    public void setMatriz(Loja matriz) {
        this.matriz = matriz;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "matriz", "nome", "telefone", "endereco", "inscricaoestadual"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Matriz", "Telefone", "Endereço", "Inscrição Estadual"};
    }

    @Override
    public Object getIdentifier() {
        return cnpj;
    }

    @Override
    public String getDatabaseLogIdentifier() {
        return cnpj;
    }

    @Override
    public String getDeleteWhereClause() {
        return "cnpj = ?";
    }
}
