package com.vandamodaintima.jfpsb.contador.sincronizacao;

import com.google.gson.annotations.SerializedName;
import com.vandamodaintima.jfpsb.contador.model.IModel;

import org.threeten.bp.ZonedDateTime;

import java.io.Serializable;

public class DBLog<T extends IModel & Serializable> {
    @SerializedName(value = "Operacao")
    private String operacao;

    @SerializedName(value = "Entidade")
    private T entidade;

    @SerializedName(value = "ModificadoEm")
    private ZonedDateTime modificadoEm;

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }

    public ZonedDateTime getModificadoEm() {
        return modificadoEm;
    }

    public void setModificadoEm(ZonedDateTime modificadoEm) {
        this.modificadoEm = modificadoEm;
    }
}
