package com.vandamodaintima.jfpsb.contador.entidade;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Contagem_Produto {
    private int id;
    private int contagem;
    private String produto;
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

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
}