package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Produto implements Serializable, IModel<Produto> {
    private transient DAOProduto daoProduto;

    public Produto() {
    }

    public Produto(ConexaoBanco conexaoBanco) {
        daoProduto = new DAOProduto(conexaoBanco);
    }

    @SerializedName(value = "CodBarra")
    private String cod_barra;
    @SerializedName(value = "Codigos")
    private ArrayList<String> cod_barra_fornecedor = new ArrayList<>();
    @SerializedName(value = "Marca")
    private Marca marca;
    @SerializedName(value = "Fornecedor")
    private Fornecedor fornecedor;
    @SerializedName(value = "Descricao")
    private String descricao;
    @SerializedName(value = "Preco")
    private Double preco;
    @SerializedName(value = "Ncm")
    private String ncm;

    public static String[] getColunas() {
        return new String[]{"cod_barra as _id", "descricao", "preco", "fornecedor", "marca", "ncm"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód. De Barra", "Descrição", "Preço", "Fornecedor", "Marca", "NCM", "Cód. de Barra de Fornecedor"};
    }

    public String getCod_barra() {
        return cod_barra;
    }

    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }

    public ArrayList<String> getCod_barra_fornecedor() {
        return cod_barra_fornecedor;
    }

    public void setCod_barra_fornecedor(ArrayList<String> cod_barra_fornecedor) {
        this.cod_barra_fornecedor = cod_barra_fornecedor;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    @Override
    public Object getIdentifier() {
        return cod_barra;
    }

    @Override
    public String getDeleteWhereClause() {
        return "cod_barra = ?";
    }

    @Override
    public Boolean salvar() {
        return daoProduto.inserir(this, true);
    }

    @Override
    public Boolean salvar(List<Produto> lista) {
        return daoProduto.inserir(lista, true);
    }

    @Override
    public Boolean atualizar() {
        return daoProduto.atualizar(this, true);
    }

    @Override
    public Boolean deletar() {
        return daoProduto.deletar(this, true);
    }

    @Override
    public List<Produto> listar() {
        return daoProduto.listar();
    }

    @Override
    public Produto listarPorId(Object... ids) {
        return daoProduto.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        Produto produto = listarPorId(ids);
        if(produto!=null) {
            cod_barra = produto.getCod_barra();
            cod_barra_fornecedor = produto.getCod_barra_fornecedor();
            marca = produto.getMarca();
            fornecedor = produto.getFornecedor();
            descricao = produto.getDescricao();
            preco = produto.getPreco();
            ncm = produto.getNcm();
        }
    }

    public ArrayList<Produto> listarPorCodBarra(String codigo) {
        return daoProduto.listarPorCodBarra(codigo);
    }

    public Cursor listarPorDescricaoCursor(String termo) {
        return daoProduto.listarPorDescricaoCursor(termo);
    }

    public Cursor listarPorCodBarraCursor(String termo) {
        return daoProduto.listarPorCodBarraCursor(termo);
    }

    public Cursor listarPorFornecedorCursor(String termo) {
        return daoProduto.listarPorFornecedorCursor(termo);
    }

    public Cursor listarPorMarcaCursor(String termo) {
        return daoProduto.listarPorMarcaCursor(termo);
    }
}
