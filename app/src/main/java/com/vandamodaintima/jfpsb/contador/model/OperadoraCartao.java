package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOOperadoraCartao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperadoraCartao implements IModel<OperadoraCartao>, Serializable {
    private transient DAOOperadoraCartao daoOperadoraCartao;

    public OperadoraCartao() {
    }

    public OperadoraCartao(ConexaoBanco conexaoBanco) {
        daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
    }

    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "IdentificadoresBanco")
    private List<String> identificadoresBanco = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getIdentificadoresBanco() {
        return identificadoresBanco;
    }

    public void setIdentificadoresBanco(List<String> identificadoresBanco) {
        this.identificadoresBanco = identificadoresBanco;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id"};
    }

    @Override
    public Object getIdentifier() {
        return nome;
    }

    @Override
    public String getDeleteWhereClause() {
        return "nome = ?";
    }

    @Override
    public Boolean salvar() {
        return daoOperadoraCartao.inserir(this, true);
    }

    @Override
    public Boolean salvar(List<OperadoraCartao> lista) {
        return daoOperadoraCartao.inserir(lista, true);
    }

    @Override
    public Boolean atualizar() {
        return daoOperadoraCartao.atualizar(this, true);
    }

    @Override
    public Boolean deletar() {
        return daoOperadoraCartao.deletar(this, true);
    }

    @Override
    public List<OperadoraCartao> listar() {
        return daoOperadoraCartao.listar();
    }

    @Override
    public OperadoraCartao listarPorId(Object... ids) {
        return daoOperadoraCartao.listarPorId(ids);
    }

    @Override
    /**
     * Carrega no objeto atual os dados da entidade retornada pelas ids fornecidas
     */
    public void load(Object... ids) {
        OperadoraCartao operadoraCartao = daoOperadoraCartao.listarPorId(ids);
        if (operadoraCartao != null) {
            setNome(operadoraCartao.getNome());
            setIdentificadoresBanco(operadoraCartao.getIdentificadoresBanco());
        }
    }
}
