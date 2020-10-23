package com.vandamodaintima.jfpsb.contador.model;

import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProdutoGrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProdutoGrade implements IModel<ProdutoGrade>, Serializable {
    private DAOProdutoGrade daoProdutoGrade;
    private String codBarra;
    private Produto produto;
    private double preco;
    private List<SubGrade> subGrades = new ArrayList<>();

    public ProdutoGrade() {
    }

    public ProdutoGrade(ConexaoBanco conexaoBanco) {
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public List<SubGrade> getSubGrades() {
        return subGrades;
    }

    public void setSubGrades(List<SubGrade> subGrades) {
        this.subGrades = subGrades;
    }

    public static String[] getColunas() {
        return new String[]{"cod_barra as _id", "produto", "preco"};
    }

    public static String[] getHeaders() {
        return new String[0];
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
    public Boolean salvar(List<ProdutoGrade> lista) {
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
    public List<ProdutoGrade> listar() {
        return null;
    }

    @Override
    public ProdutoGrade listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}