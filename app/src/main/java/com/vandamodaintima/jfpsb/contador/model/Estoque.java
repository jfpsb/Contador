package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.util.List;

public class Estoque implements IModel<Estoque>, Serializable {
    private ProdutoGrade produtoGrade;
    private Loja loja;
    private int quantidade;

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public Object getIdentifier() {
        return null;
    }

    @Override
    public String getDeleteWhereClause() {
        return null;
    }

    @Override
    public Boolean salvar() {
        return null;
    }

    @Override
    public Boolean salvar(List<Estoque> lista) {
        return null;
    }

    @Override
    public Boolean atualizar() {
        return null;
    }

    @Override
    public Boolean deletar() {
        return null;
    }

    @Override
    public List<Estoque> listar() {
        return null;
    }

    @Override
    public Estoque listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }

    public static String[] getColunas() {
        return new String[0];
    }

    public static String[] getHeaders() {
        return new String[0];
    }
}
