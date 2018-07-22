package com.vandamodaintima.jfpsb.contador.entidade;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class ContagemProduto {
    private int id;
    private Contagem contagem;
    private Produto produto;
    private int quant;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return new String[] { "id as _id", "contagem", "produto", "quant" };
    }
}
