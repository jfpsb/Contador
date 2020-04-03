package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;

import java.io.Serializable;
import java.util.List;

public class Marca implements Serializable, IModel<Marca> {
    private DAOMarca daoMarca;

    public Marca(){}

    public Marca(ConexaoBanco conexaoBanco) {
        daoMarca = new DAOMarca(conexaoBanco);
    }

    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "Fornecedor")
    private Fornecedor fornecedor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id", "fornecedor"};
    }

    public static String[] getHeaders() {
        return new String[]{"Nome", "Fornecedor"};
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
        return null;
    }

    @Override
    public Boolean salvar(List<Marca> lista) {
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
    public List<Marca> listar() {
        return null;
    }

    @Override
    public Marca listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}
