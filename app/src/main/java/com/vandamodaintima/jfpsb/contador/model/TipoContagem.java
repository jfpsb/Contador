package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;

import java.io.Serializable;
import java.util.List;

public class TipoContagem implements Serializable, IModel<TipoContagem> {
    private DAOTipoContagem daoTipoContagem;

    @SerializedName(value = "Id")
    private long id;
    @SerializedName(value = "Nome")
    private String nome;

    public TipoContagem() {
    }

    public TipoContagem(ConexaoBanco conexaoBanco) {
        daoTipoContagem = new DAOTipoContagem(conexaoBanco);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String[] getColunas() {
        return new String[]{"id as _id", "nome"};
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
        return null;
    }

    @Override
    public Boolean salvar(List<TipoContagem> lista) {
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
    public List<TipoContagem> listar() {
        return null;
    }

    @Override
    public TipoContagem listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}
