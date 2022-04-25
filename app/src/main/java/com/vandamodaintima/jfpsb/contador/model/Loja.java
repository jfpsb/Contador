package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Loja extends AModel implements Serializable, IModel<Loja> {
    private transient DAOLoja daoLoja;

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
    private double aluguel;

    public Loja() {
    }

    public Loja(String nome) {
        this.nome = nome;
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

    public double getAluguel() {
        return aluguel;
    }

    public void setAluguel(double aluguel) {
        this.aluguel = aluguel;
    }

    public static String[] getColunas() {
        return new String[]{"cnpj as _id", "matriz", "nome", "telefone", "endereco", "inscricaoestadual", "aluguel", "deletado"};
    }

    public static String[] getHeaders() {
        return new String[]{"CNPJ", "Nome", "Matriz", "Telefone", "Endereço", "Inscrição Estadual", "Aluguel"};
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
        return daoLoja.inserir(this);
    }

    @Override
    public Boolean salvar(List<Loja> lista) {
        return daoLoja.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoLoja.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoLoja.deletar(this);
    }

    @Override
    public List<Loja> listar() {
        return daoLoja.listar();
    }

    @Override
    public Loja listarPorId(Object... ids) {
        return daoLoja.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        Loja loja = listarPorId(ids);
        if (loja != null) {
            cnpj = loja.getCnpj();
            nome = loja.getNome();
            endereco = loja.getEndereco();
            telefone = loja.getTelefone();
            matriz = loja.getMatriz();
            inscricaoEstadual = loja.getInscricaoEstadual();
        }
    }

    public ArrayList<Loja> listarMatrizes() {
        return daoLoja.listarMatrizes();
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return daoLoja.listarPorNomeCnpjCursor(termo);
    }
}
