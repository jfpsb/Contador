package com.vandamodaintima.jfpsb.contador.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.dao.DAORecebimentoCartao;

import java.io.Serializable;
import java.util.List;

public class RecebimentoCartao implements IModel<RecebimentoCartao>, Serializable {
    private transient DAORecebimentoCartao daoRecebimentoCartao;

    public RecebimentoCartao() {
    }

    public RecebimentoCartao(ConexaoBanco conexaoBanco) {
        daoRecebimentoCartao = new DAORecebimentoCartao(conexaoBanco);
    }

    @SerializedName(value = "Mes")
    private int mes;
    @SerializedName(value = "Ano")
    private int ano;
    @SerializedName(value = "Loja")
    private Loja loja;
    @SerializedName(value = "OperadoraCartao")
    private OperadoraCartao operadoraCartao;
    @SerializedName(value = "Recebido")
    private double recebido;
    @SerializedName(value = "ValoOperadora")
    private double valorOperadora;
    @SerializedName(value = "Observacao")
    private String observacao;

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public OperadoraCartao getOperadoraCartao() {
        return operadoraCartao;
    }

    public void setOperadoraCartao(OperadoraCartao operadoraCartao) {
        this.operadoraCartao = operadoraCartao;
    }

    public double getRecebido() {
        return recebido;
    }

    public void setRecebido(double recebido) {
        this.recebido = recebido;
    }

    public double getValorOperadora() {
        return valorOperadora;
    }

    public void setValorOperadora(double valorOperadora) {
        this.valorOperadora = valorOperadora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public static String[] getColunas() {
        return new String[]{"mes, ano, loja, operadoracartao, recebido, valorOperadora, observacao"};
    }

    @Override
    public Object getIdentifier() {
        return new String[]{String.valueOf(mes), String.valueOf(ano), String.valueOf(loja.getIdentifier()), String.valueOf(operadoraCartao.getIdentifier())};
    }

    @Override
    public String getDeleteWhereClause() {
        return "mes = ? AND ano = ? AND loja = ? AND operadoracartao = ?";
    }

    @Override
    public Boolean salvar() {
        return daoRecebimentoCartao.inserir(this, true);
    }

    @Override
    public Boolean salvar(List<RecebimentoCartao> lista) {
        return daoRecebimentoCartao.inserir(lista, true);
    }

    @Override
    public Boolean atualizar() {
        return daoRecebimentoCartao.atualizar(this, true);
    }

    @Override
    public Boolean deletar() {
        return daoRecebimentoCartao.deletar(this, true);
    }

    @Override
    public List<RecebimentoCartao> listar() {
        return daoRecebimentoCartao.listar();
    }

    @Override
    public RecebimentoCartao listarPorId(Object... ids) {
        return daoRecebimentoCartao.listarPorId(ids);
    }

    @Override
    public void load(Object... ids) {
        RecebimentoCartao recebimentoCartao = listarPorId(ids);
        if (recebimentoCartao != null) {
            mes = recebimentoCartao.getMes();
            ano = recebimentoCartao.getAno();
            loja = recebimentoCartao.getLoja();
            operadoraCartao = recebimentoCartao.getOperadoraCartao();
            recebido = recebimentoCartao.getRecebido();
            valorOperadora = recebimentoCartao.getValorOperadora();
            observacao = recebimentoCartao.getObservacao();
        }
    }
}
