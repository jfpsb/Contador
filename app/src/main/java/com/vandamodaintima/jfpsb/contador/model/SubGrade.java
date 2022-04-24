package com.vandamodaintima.jfpsb.contador.model;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOSubGrade;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class SubGrade extends AModel implements IModel<SubGrade>, Serializable {
    private transient DAOSubGrade daoSubGrade;

    private UUID id;
    private ProdutoGrade produtoGrade;
    private Grade grade;

    public SubGrade() {
    }

    public SubGrade(ConexaoBanco conexaoBanco) {
        daoSubGrade = new DAOSubGrade(conexaoBanco);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
        return new String[]{"uuid as _id","produto_grade", "grade"};
    }

    public static String[] getHeaders() {
        return new String[0];
    }
}
