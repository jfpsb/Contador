package com.vandamodaintima.jfpsb.contador.entidade;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem_Produto {
    private Contagem contagem;
    private Produto produto;
    private int quant;

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
}
