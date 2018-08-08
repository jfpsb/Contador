package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;

public class CodBarraFornecedor implements Serializable {
    private Produto produto;
    private String codigo;

    private static final String[] colunas = new String[] { "ROWID as _id", "produto", "codigo" };

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public static String[] getColunas() {
        return colunas;
    }
}
