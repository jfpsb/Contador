package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ContagemProduto extends AModel implements Serializable, IModel<ContagemProduto> {
    private transient DAOContagemProduto daoContagemProduto;

    public ContagemProduto() {
    }

    public ContagemProduto(ConexaoBanco conexaoBanco) {
        daoContagemProduto = new DAOContagemProduto(conexaoBanco);
    }

    @SerializedName(value = "Id")
    private UUID id;
    @SerializedName(value = "Contagem")
    private Contagem contagem;
    @SerializedName(value = "ProdutoGrade")
    private ProdutoGrade produtoGrade;
    @SerializedName(value = "Quant")
    private int quant;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Contagem getContagem() {
        return contagem;
    }

    public void setContagem(Contagem contagem) {
        this.contagem = contagem;
    }

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public static String[] getColunas() {
        return new String[]{"uuid as _id", "produto_grade", "contagem", "quant"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód. De Barra", "Cód. De Barra Alt.", "Descrição", "Preço De Custo", "Preço De Venda", "Quantidade"};
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
        return daoContagemProduto.inserir(this);
    }

    @Override
    public Boolean salvar(List<ContagemProduto> lista) {
        return daoContagemProduto.inserir(lista);
    }

    @Override
    public Boolean atualizar() {
        return daoContagemProduto.atualizar(this);
    }

    @Override
    public Boolean deletar() {
        return daoContagemProduto.deletar(this);
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
            produtoGrade = contagemProduto.getProdutoGrade();
            quant = contagemProduto.getQuant();
        }
    }

    public Object listarPorContagemGroupByProduto() {
        return daoContagemProduto.listarPorContagemGroupByProduto(contagem);
    }

    public List<ContagemProduto> listarPorContagemGroupByProduto(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProduto(contagemModel);
    }

    public List<ContagemProduto> listarPorContagemGroupByGrade(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProdutoGrade(contagemModel);
    }

    public Cursor listarPorContagemCursor(Contagem contagem) {
        return daoContagemProduto.listarPorContagemCursor(contagem);
    }

    public List<ContagemProduto> listarPorContagem(Contagem contagem) {
        return daoContagemProduto.listarPorContagem(contagem);
    }

    public Cursor listarPorContagemGroupByProdutoCursor(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProdutoCursor(contagemModel);
    }

    public Cursor listarPorContagemGroupByGradeCursor(Contagem contagemModel) {
        return daoContagemProduto.listarPorContagemGroupByProdutoGradeCursor(contagemModel);
    }
}
