package com.vandamodaintima.jfpsb.contador.model;

import java.io.Serializable;
import java.util.List;

public class Grade implements IModel<Grade>, Serializable {
    private int id;
    private TipoGrade tipoGrade;
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoGrade getTipoGrade() {
        return tipoGrade;
    }

    public void setTipoGrade(TipoGrade tipoGrade) {
        this.tipoGrade = tipoGrade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
    public Boolean salvar(List<Grade> lista) {
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
    public List<Grade> listar() {
        return null;
    }

    @Override
    public Grade listarPorId(Object... ids) {
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
