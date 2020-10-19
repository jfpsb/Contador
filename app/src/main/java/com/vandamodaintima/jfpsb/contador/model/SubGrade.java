package com.vandamodaintima.jfpsb.contador.model;

import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.io.Serializable;
import java.util.List;

public class SubGrade implements IModel<SubGrade>, Serializable {
    private ProdutoGrade produtoGrade;
    private Grade grade;

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
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
    public Boolean salvar(List<SubGrade> lista) {
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
    public List<SubGrade> listar() {
        return null;
    }

    @Override
    public SubGrade listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }

    public static String[] getColunas() {
        return new String[]{"produto_grade", "grade"};
    }

    public static String[] getHeaders() {
        return new String[0];
    }
}
