package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Fornecedor implements Serializable, IModel {
    private String cnpj;
    private String nome;
    private String fantasia;
    private String email;
    private String telefone;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "nome", "fantasia", "email", "telefone"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Nome Fantasia", "Email", "Telefone"};
    }

    @Override
    public String getIdentificador() {
        return cnpj;
    }
}
