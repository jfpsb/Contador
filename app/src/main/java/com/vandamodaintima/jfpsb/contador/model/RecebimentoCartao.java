package com.vandamodaintima.jfpsb.contador.model;

public class RecebimentoCartao implements IModel {
    private int mes;
    private int ano;
    private Loja loja;
    private OperadoraCartao operadoraCartao;
    private double recebido;
    private double valorOperadora;
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

    @Override
    public String getIdentifier() {
        return String.valueOf(mes) + ano + loja.getIdentifier() + operadoraCartao.getIdentifier();
    }
}
