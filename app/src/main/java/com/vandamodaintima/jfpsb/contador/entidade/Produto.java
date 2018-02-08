package com.vandamodaintima.jfpsb.contador.entidade;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Produto {
    private int cod_barra;
    private Fornecedor fornecedor;
    private String descricao;
    private Double preco;

    public int getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(int cod_barra) {
        this.cod_barra = cod_barra;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
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
}
