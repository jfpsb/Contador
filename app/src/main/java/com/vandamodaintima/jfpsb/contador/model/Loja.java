package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;

public class Loja implements Serializable {
    private String cnpj;
    private Loja matriz;
    private String nome;
    private String telefone;

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

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "matriz", "nome", "telefone"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Matriz", "Telefone"};
    }
}
