package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;

import java.io.Serializable;
import java.util.List;

public class ContagemProduto implements Serializable, IModel<ContagemProduto> {
    private transient DAOContagemProduto daoContagemProduto;

    public ContagemProduto() {
    }

    public ContagemProduto(ConexaoBanco conexaoBanco) {
        daoContagemProduto = new DAOContagemProduto(conexaoBanco);
    }

    @SerializedName(value = "Id")
    private long id;
    @SerializedName(value = "Contagem")
    private Contagem contagem;
    @SerializedName(value = "Produto")
    private Produto produto;
    @SerializedName(value = "Quant")
    private int quant;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contagem getContagem() {
        return contagem;
    }

    public void setContagem(Contagem contagem) {
        this.contagem = contagem;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public static String[] getColunas() {
        return new String[]{"id as _id", "produto", "contagem_data", "contagem_loja", "quant"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód De Barra", "Descrição", "Preço", "Quantidade"};
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
        return daoContagemProduto.inserir(this, true);
    }

    @Override
    public Boolean salvar(List<ContagemProduto> lista) {
        return daoContagemProduto.inserir(lista, true);
    }

    @Override
    public Boolean atualizar() {
        return daoContagemProduto.atualizar(this, true);
    }

    @Override
    public Boolean deletar() {
        return daoContagemProduto.deletar(this, true);
    }

    @Override
    public List<ContagemProduto> listar() {
        return daoContagemProduto.listar();
    }

    @Override
    public ContagemProduto listarPorId(Object... ids) {
        return daoContagemProduto.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        ContagemProduto contagemProduto = listarPorId(ids);
        if (contagemProduto != null) {
            id = contagemProduto.getId();
            contagem = contagemProduto.getContagem();
            produto = contagemProduto.getProduto();
            quant = contagemProduto.getQuant();
        }
    }

    public Object listarPorContagemGroupByProduto() {
        return daoContagemProduto.listarPorContagemGroupByProduto(contagem);
    }

    public Object listarPorContagemGroupByProduto(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProduto(contagemModel);
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        return daoContagemProduto.listarPorContagemCursor(contagem);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProdutoCursor(contagemModel);
    }
}
