package com.vandamodaintima.jfpsb.contador.model;

public class ProdutoContagem {
    private long id;
    private Contagem contagem;
    private Produto produto;
    private int quant;

    private static final String[] colunas = new String[] { "id as _id", "produto", "contagem_data", "contagem_loja", "quant" };

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
        return colunas;
    }
}
