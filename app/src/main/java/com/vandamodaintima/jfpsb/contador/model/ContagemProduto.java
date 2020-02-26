package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContagemProduto implements Serializable, IModel {
    @SerializedName(value = "Id")
    private long id;
    @SerializedName(value = "Contagem")
    private Contagem contagem;
    @SerializedName(value = "Produto")
    private Produto produto;
    @SerializedName(value = "Quant")
    private int quant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contagem getContagem() {
        return contagem;
    }

    public void setContagem(Contagem contagem) {
        this.contagem = contagem;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public static String[] getColunas() {
        return new String[]{"id as _id", "produto", "contagem_data", "contagem_loja", "quant"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód De Barra", "Descrição", "Preço", "Quantidade"};
    }

    @Override
    public String getIdentifier() {
        return String.valueOf(id);
    }
}
