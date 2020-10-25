package com.vandamodaintima.jfpsb.contador.model;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOGrade;

import java.io.Serializable;
import java.util.List;

public class Grade implements IModel<Grade>, Serializable {
    private transient DAOGrade daoGrade;

    private int id;
    private TipoGrade tipoGrade;
    private String nome;

    public Grade() {
    }

    public Grade(ConexaoBanco conexaoBanco) {
        daoGrade = new DAOGrade(conexaoBanco);
    }

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
        return id;
    }

    @Override
    public String getDeleteWhereClause() {
        return "id = ?";
    }

    @Override
    public Boolean salvar() {
        return daoGrade.inserir(this);
    }

    @Override
    public Boolean salvar(List<Grade> lista) {
        return daoGrade.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoGrade.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoGrade.deletar(this);
    }

    @Override
    public List<Grade> listar() {
        return daoGrade.listar();
    }

    @Override
    public Grade listarPorId(Object... ids) {
        return daoGrade.listarPorId(ids);
    }

    public List<Grade> listarPorTipoGrade(TipoGrade tipoGrade) {
        return daoGrade.listarPorTipoGrade(tipoGrade);
    }

    @Override
    public void load(Object... ids) {
        Grade grade = listarPorId(ids);
        if (grade != null) {
            this.id = grade.id;
            this.tipoGrade = grade.tipoGrade;
            this.nome = grade.nome;
        }
    }

    public static String[] getColunas() {
        return new String[]{"id as _id", "nome", "tipo"};
    }

    public static String[] getHeaders() {
        return new String[]{"Nome", "Tipo de Grade"};
    }
}
