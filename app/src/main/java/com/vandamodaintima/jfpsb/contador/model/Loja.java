package com.vandamodaintima.jfpsb.contador.model;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class Loja implements Serializable, IModel {
    @Element(name = "Cnpj")
    private String cnpj;
    @Element(name = "Matriz", required = false)
    private Loja matriz;
    @Element(name = "Nome")
    private String nome;
    @Element(name = "Telefone", required = false)
    private String telefone;
    @Element(name = "Endereco", required = false)
    private String endereco;
    @Element(name = "InscricaoEstadual", required = false)
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
