package com.vandamodaintima.jfpsb.contador.model;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProdutoGrade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProdutoGrade extends AModel implements IModel<ProdutoGrade>, Serializable {
    private transient DAOProdutoGrade daoProdutoGrade;
    private UUID id;
    private String codBarra;
    private String codBarraAlternativo;
    private Produto produto;
    private double preco_custo;
    private double preco_venda;
    private List<SubGrade> grades = new ArrayList<>();

    public ProdutoGrade() {
    }

    public ProdutoGrade(ConexaoBanco conexaoBanco) {
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getCodBarraAlternativo() {
        return codBarraAlternativo;
    }

    public void setCodBarraAlternativo(String codBarraAlternativo) {
        this.codBarraAlternativo = codBarraAlternativo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getPreco_custo() {
        return preco_custo;
    }

    public void setPreco_custo(double preco_custo) {
        this.preco_custo = preco_custo;
    }

    public double getPreco_venda() {
        return preco_venda;
    }

    public void setPreco_venda(double preco_venda) {
        this.preco_venda = preco_venda;
    }

    public List<SubGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<SubGrade> grades) {
        this.grades = grades;
    }

    public static String[] getColunas() {
        return new String[]{"uuid as _id", "cod_barra", "cod_barra_alternativo", "produto", "preco_venda", "preco_custo"};
    }

    public static String[] getHeaders() {
        return new String[0];
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
        return null;
    }

    @Override
    public Boolean salvar(List<ProdutoGrade> lista) {
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
    public List<ProdutoGrade> listar() {
        return null;
    }

    @Override
    public ProdutoGrade listarPorId(Object... ids) {
        return null;
    }

    public List<ProdutoGrade> listarPorCodBarra(String codBarra) {
        return daoProdutoGrade.listarPorCodBarra(codBarra);
    }

    @Override
    public void load(Object... ids) {

    }

    public String getGradesToString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < grades.size(); i++) {
            str.append(grades.get(i).getGrade().getTipoGrade().getNome()).append(" ").append(grades.get(i).getGrade().getNome());
            if (i != grades.size() - 1) {
                str.append("/");
            }
        }
        return str.toString();
    }

    public String getGradesToShortString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < grades.size(); i++) {
            str.append(grades.get(i).getGrade().getNome());
            if (i != grades.size() + 1) {
                str.append(" ");
            }
        }
        return str.toString();
    }
}
