package com.vandamodaintima.jfpsb.contador.entidade;

import java.io.Serializable;
import java.util.Objects;

public class CodBarraFornecedor implements Serializable {
    private Produto produto;
    private String codigo;

    private static final String[] colunas = new String[] { "rowid as _id", "produto", "codigo" };

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

    @Override
    public boolean equals(Object o) {
        if(o instanceof CodBarraFornecedor) {
            if(((CodBarraFornecedor) o).getProduto() != null && ((CodBarraFornecedor) o).getProduto().getCod_barra() != null) {
                if(((CodBarraFornecedor) o).getCodigo().equals(codigo) && ((CodBarraFornecedor) o).getProduto().getCod_barra().equals(produto.getCod_barra())) {
                    return true;
                }
            } else {
                if(((CodBarraFornecedor) o).getCodigo().equals(codigo)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(produto, codigo);
    }
}
