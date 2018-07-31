package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Fornecedor implements Serializable {
    private String cnpj;
    private String nome;
    private String fantasia;

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

    public static String[] getColunas() {
        return new String[] { "cnpj as _id", "nome", "fantasia"};
    }
}
