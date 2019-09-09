package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Produto implements Serializable {
    private String cod_barra;
    private ArrayList<String> cod_barra_fornecedor = new ArrayList<>();
    private Marca marca;
    private Fornecedor fornecedor;
    private String descricao;
    private Double preco;

    public static String[] getColunas() {
        return new String[]{"cod_barra as _id", "descricao", "preco", "fornecedor", "marca"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód. De Barra", "Descrição", "Preço", "Fornecedor", "Marca", "Cód. de Barra de Fornecedor"};
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    public ArrayList<String> getCod_barra_fornecedor() {
        return cod_barra_fornecedor;
    }

    public void setCod_barra_fornecedor(ArrayList<String> cod_barra_fornecedor) {
        this.cod_barra_fornecedor = cod_barra_fornecedor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
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
