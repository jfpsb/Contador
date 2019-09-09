package com.vandamodaintima.jfpsb.contador.view.contagem;

import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemCodigoBarraLido implements Serializable {
    private Status status;
    private String codigo;
    private ArrayList<Produto> produtos;

    public enum Status {
        ENCONTRADO("Produto Encontrado e Contagem Adicionada"),
        NAO_ENCONTRADO("Produto Não Encontrado. Aperte Para Resolver"),
        VARIOS_ENCONTRADOS("Vários Produtos Encontrados. Aperte para Resolver");

        private String value;

        Status(String s) {
            value = s;
        }

        public String getValue() {
            return value;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }
}
