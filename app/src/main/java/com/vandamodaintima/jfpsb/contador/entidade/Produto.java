package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;
import java.util.ArrayList;

public class Produto implements Serializable {
    private String cod_barra;
    private ArrayList<CodBarraFornecedor> cod_barra_fornecedor = new ArrayList<>();
    private Marca marca;
    private Fornecedor fornecedor;
    private String descricao;
    private Double preco;

    private static final String[] colunas = new String[]{"cod_barra as _id", "fornecedor", "marca", "descricao", "preco"};
    private static String[] headers = new String[]{"Cód. de Barras", "Cód. de Barras Fornecedor", "Fornecedor", "Marca", "Descrição", "Preço"};

    public ArrayList<CodBarraFornecedor> getCod_barra_fornecedor() {
        return cod_barra_fornecedor;
    }

    public void setCod_barra_fornecedor(ArrayList<CodBarraFornecedor> cod_barra_fornecedor) {
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
        return colunas;
    }

    public static String[] getHeaders() {
        return headers;
    }

    /**
     * Cria cópia do produto passado como parâmetro
     * @param p Produto a ser copiado para novo objeto
     * @return Cópia do produto em novo objeto
     */
    public static Produto criar(Produto p) {
        Produto produto = new Produto();

        produto.setCod_barra(p.getCod_barra());
        produto.setDescricao(p.getDescricao());
        produto.setFornecedor(p.getFornecedor());
        produto.setPreco(p.getPreco());
        produto.setCod_barra_fornecedor(p.getCod_barra_fornecedor());
        produto.setMarca(p.getMarca());

        return produto;
    }
}
