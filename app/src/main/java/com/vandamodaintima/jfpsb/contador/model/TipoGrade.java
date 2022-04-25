package com.vandamodaintima.jfpsb.contador.model;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoGrade;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class TipoGrade extends AModel implements IModel<TipoGrade>, Serializable {
    private transient DAOTipoGrade daoTipoGrade;

    private UUID id;
    private String nome;

    public TipoGrade() {
    }

    public TipoGrade(ConexaoBanco conexaoBanco) {
        daoTipoGrade = new DAOTipoGrade(conexaoBanco);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        return "uuid = ?";
    }

    @Override
    public Boolean salvar() {
        return daoTipoGrade.inserir(this);
    }

    @Override
    public Boolean salvar(List<TipoGrade> lista) {
        return daoTipoGrade.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoTipoGrade.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoTipoGrade.deletar(this);
    }

    @Override
    public List<TipoGrade> listar() {
        return daoTipoGrade.listar();
    }

    @Override
    public TipoGrade listarPorId(Object... ids) {
        return daoTipoGrade.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        TipoGrade tipoGrade = listarPorId(ids);
        if (tipoGrade != null) {
            this.id = tipoGrade.id;
            this.nome = tipoGrade.nome;
        }
    }

    public static String[] getColunas() {
        return new String[]{"uuid as _id", "nome", "deletado"};
    }

    public static String[] getHeaders() {
        return new String[]{"Nome"};
    }
}
