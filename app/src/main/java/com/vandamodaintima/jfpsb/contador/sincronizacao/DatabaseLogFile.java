package com.vandamodaintima.jfpsb.contador.sincronizacao;

import com.vandamodaintima.jfpsb.contador.model.IModel;

import org.threeten.bp.ZonedDateTime;

public class DatabaseLogFile<T extends IModel> {
    private String OperacaoMySQL;
    private T Entidade;
    private ZonedDateTime LastWriteTime;

    public String getOperacaoMySQL() {
        return OperacaoMySQL;
    }

    public void setOperacaoMySQL(String operacaoMySQL) {
        this.OperacaoMySQL = operacaoMySQL;
    }

    public T getEntidade() {
        return Entidade;
    }

    public void setEntidade(T entidade) {
        this.Entidade = entidade;
    }

    public ZonedDateTime getLastWriteTime() {
        return LastWriteTime;
    }

    public void setLastWriteTime(ZonedDateTime lastWriteTime) {
        LastWriteTime = lastWriteTime;
    }

    public String getFileName() {
        return Entidade.getClass().getSimpleName() + " " + Entidade.getIdentifier() + ".json";
    }
}
