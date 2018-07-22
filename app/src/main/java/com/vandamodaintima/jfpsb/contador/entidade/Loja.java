package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Loja implements Serializable {
    private String cnpj;
    private String nome;

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

    public static String[] getColunas() {
        return new String[] { "cnpj as _id", "nome" };
    }
}
