package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Fornecedor implements Serializable, IModel<Fornecedor> {
    private DAOFornecedor daoFornecedor;

    public Fornecedor(){}

    public Fornecedor(ConexaoBanco conexaoBanco) {
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @SerializedName(value = "Cnpj")
    private String cnpj;
    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "Fantasia")
    private String fantasia;
    @SerializedName(value = "Email")
    private String email;
    @SerializedName(value = "Telefone")
    private String telefone;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "nome", "fantasia", "email", "telefone"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Nome Fantasia", "Email", "Telefone"};
    }

    @Override
    public Object getIdentifier() {
        return cnpj;
    }

    @Override
    public String getDeleteWhereClause() {
        return "cnpj = ?";
    }

    @Override
    public Boolean salvar() {
        return null;
    }

    @Override
    public Boolean salvar(List<Fornecedor> lista) {
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
    public List<Fornecedor> listar() {
        return null;
    }

    @Override
    public Fornecedor listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }
}
