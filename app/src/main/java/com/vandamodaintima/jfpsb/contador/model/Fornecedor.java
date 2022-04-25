package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class Fornecedor extends AModel implements Serializable, IModel<Fornecedor> {
    private transient DAOFornecedor daoFornecedor;

    public Fornecedor() {
    }

    public Fornecedor(String nome) {
        this.nome = nome;
    }

    public Fornecedor(ConexaoBanco conexaoBanco) {
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @SerializedName(value = "Cnpj")
    private String cnpj;
    private Representante representante;
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

    public Representante getRepresentante() {
        return representante;
    }

    public void setRepresentante(Representante representante) {
        this.representante = representante;
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
        return new String[]{"cnpj as _id", "nome", "fantasia", "email", "telefone", "deletado"};
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
        return daoFornecedor.inserir(this);
    }

    @Override
    public Boolean salvar(List<Fornecedor> lista) {
        return daoFornecedor.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoFornecedor.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoFornecedor.deletar(this);
    }

    @Override
    public List<Fornecedor> listar() {
        return daoFornecedor.listar();
    }

    @Override
    public Fornecedor listarPorId(Object... ids) {
        return daoFornecedor.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        Fornecedor fornecedor = listarPorId(ids);
        if(fornecedor!= null) {
            cnpj = fornecedor.getCnpj();
            nome = fornecedor.getNome();
            fantasia = fornecedor.getFantasia();
            email = fornecedor.getEmail();
            telefone = fornecedor.getTelefone();
        }
    }

    public Fornecedor listarPorIdOuNome(String stringCellValue) {
        return daoFornecedor.listarPorIdOuNome(stringCellValue);
    }

    public Cursor listarPorCnpjNomeFantasiaCursor(String termo) {
        return daoFornecedor.listarPorCnpjNomeFantasiaCursor(termo);
    }
}
