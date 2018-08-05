package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Produto implements Serializable {
    private String cod_barra;
    private String cod_barra_fornecedor;
    private Marca marca;
    private Fornecedor fornecedor;
    private String descricao;
    private Double preco;

    private static String[] headers = new String[] { "Cód. de Barras", "Cód. de Barras Fornecedor", "Fornecedor", "Marca", "Descrição", "Preço" };

    public String getCod_barra_fornecedor() {
        return cod_barra_fornecedor;
    }

    public void setCod_barra_fornecedor(String cod_barra_fornecedor) {
        this.cod_barra_fornecedor = cod_barra_fornecedor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
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

    public static String[] getColunas() {
        return new String[]{ "cod_barra as _id", "cod_barra_fornecedor", "marca", "fornecedor", "descricao", "preco" };
    }

    public static String[] getHeaders() {
        return headers;
    }
}
