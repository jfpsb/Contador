package com.vandamodaintima.jfpsb.contador.entidade;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem_Produto {
    private int id;
    private int contagem;
    private int produto;
    private int quant;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContagem() {
        return contagem;
    }

    public void setContagem(int contagem) {
        this.contagem = contagem;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
}
