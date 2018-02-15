package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Produto implements Serializable {
    private int cod_barra;
    private String fornecedor;
    private String descricao;
    private Double preco;

    public int getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(int cod_barra) {
        this.cod_barra = cod_barra;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public static String[] getProdutoColunas() {
        return new String[] {"cod_barra as _id", "fornecedor", "descricao", "preco"};
    }
}
