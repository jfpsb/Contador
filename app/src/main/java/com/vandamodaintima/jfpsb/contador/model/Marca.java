package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Marca extends AModel implements Serializable, IModel<Marca> {
    private transient DAOMarca daoMarca;

    public Marca() {
    }

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
        return new String[]{"nome as _id", "fornecedor", "deletado"};
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
        return daoMarca.inserir(this);
    }

    @Override
    public Boolean salvar(List<Marca> lista) {
        return daoMarca.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoMarca.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoMarca.deletar(this);
    }

    @Override
    public List<Marca> listar() {
        return daoMarca.listar();
    }

    @Override
    public Marca listarPorId(Object... ids) {
        return daoMarca.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        Marca marca = listarPorId(ids);
        if (marca != null) {
            nome = marca.getNome();
            fornecedor = marca.getFornecedor();
        }
    }

    public Cursor listarPorNomeCursor(String nome) {
        return daoMarca.listarPorNomeCursor(nome);
    }

    public ArrayList<Marca> listarPorNome(String nome) {
        return daoMarca.listarPorNome(nome);
    }
}
