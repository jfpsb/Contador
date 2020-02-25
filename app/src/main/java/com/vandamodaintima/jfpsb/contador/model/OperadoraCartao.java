package com.vandamodaintima.jfpsb.contador.model;

import java.util.ArrayList;
import java.util.List;

public class OperadoraCartao implements IModel {
    private String nome;
    private List<String> identificadoresBanco = new ArrayList<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getIdentificadoresBanco() {
        return identificadoresBanco;
    }

    public void setIdentificadoresBanco(List<String> identificadoresBanco) {
        this.identificadoresBanco = identificadoresBanco;
    }

    public static String[] getColunas() {
        return new String[]{"nome as _id"};
    }

    @Override
    public String getIdentifier() {
        return nome;
    }
}
