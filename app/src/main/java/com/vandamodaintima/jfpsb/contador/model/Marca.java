package com.vandamodaintima.jfpsb.contador.model;

import org.simpleframework.xml.Element;

import java.io.Serializable;

public class Marca implements Serializable, IModel {
    @Element(name = "Nome")
    private String nome;
    @Element(name = "Fornecedor", required = false)
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
    public String getIdentificador() {
        return nome;
    }
}
