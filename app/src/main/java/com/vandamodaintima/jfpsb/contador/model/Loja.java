package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Loja implements Serializable, IModel<Loja> {
    private DAOLoja daoLoja;

    @SerializedName(value = "Cnpj")
    private String cnpj;
    @SerializedName(value = "Matriz")
    private Loja matriz;
    @SerializedName(value = "Nome")
    private String nome;
    @SerializedName(value = "Telefone")
    private String telefone;
    @SerializedName(value = "Endereco")
    private String endereco;
    @SerializedName(value = "InscricaoEstadual")
    private String inscricaoEstadual;

    public Loja() {
    }

    public Loja(ConexaoBanco conexaoBanco) {
        daoLoja = new DAOLoja(conexaoBanco);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Loja getMatriz() {
        return matriz;
    }

    public void setMatriz(Loja matriz) {
        this.matriz = matriz;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "matriz", "nome", "telefone", "endereco", "inscricaoestadual"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Matriz", "Telefone", "Endereço", "Inscrição Estadual"};
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
    public Boolean salvar(List<Loja> lista) {
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
    public List<Loja> listar() {
        return null;
    }

    @Override
    public Loja listarPorId(Object... ids) {
        return null;
    }

    @Override
    public void load(Object... ids) {

    }

    public ArrayList<Loja> listarMatrizes() {
        return daoLoja.listarMatrizes();
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return daoLoja.listarPorNomeCnpjCursor(termo);
    }
}
