package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;

public class Loja implements Serializable, IModel {
    private String cnpj;
    private Loja matriz;
    private String nome;
    private String telefone;
    private String endereco;
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
    public String getIdentificador() {
        return cnpj;
    }
}
