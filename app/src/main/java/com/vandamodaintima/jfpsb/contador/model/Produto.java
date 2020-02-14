package com.vandamodaintima.jfpsb.contador.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.ArrayList;

public class Produto implements Serializable, IModel {
    @Element(name = "Cod_Barra")
    private String cod_barra;
    @ElementList(name = "Codigos", entry = "Codigo", required = false)
    private ArrayList<String> cod_barra_fornecedor = new ArrayList<>();
    @Element(name = "Marca", required = false)
    private Marca marca;
    @Element(name = "Fornecedor", required = false)
    private Fornecedor fornecedor;
    @Element(name = "Descricao")
    private String descricao;
    @Element(name = "Preco")
    private Double preco;
    @Element(name = "Ncm", required = false)
    private String ncm;

    public static String[] getColunas() {
        return new String[]{"cod_barra as _id", "descricao", "preco", "fornecedor", "marca", "ncm"};
    }

    public static String[] getHeaders() {
        return new String[]{"Cód. De Barra", "Descrição", "Preço", "NCM", "Fornecedor", "Marca", "Cód. de Barra de Fornecedor"};
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
    public String getIdentificador() {
        return cod_barra;
    }
}
