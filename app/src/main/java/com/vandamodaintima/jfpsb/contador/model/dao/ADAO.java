package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.sincronizacao.OperacoesJsonDatabaseLog;
import com.vandamodaintima.jfpsb.contador.sincronizacao.ZonedDateTimeGsonAdapter;

import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.util.List;

public abstract class ADAO<T extends IModel> {
    protected String TABELA;
    protected ConexaoBanco conexaoBanco;
    private OperacoesJsonDatabaseLog operacoesJsonDatabaseLog;

    ADAO() {
        File dirDatabaseLog = conexaoBanco.getContext().getDir("DatabaseLog", Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeGsonAdapter())
                .create();
        operacoesJsonDatabaseLog = new OperacoesJsonDatabaseLog(gson, dirDatabaseLog);
    }

    public Boolean inserir(T t) {
        operacoesJsonDatabaseLog.escreverJson("INSERT", t);
        return true;
    }

    public Boolean inserir(List<T> lista) {
        for(T t : lista) {
            operacoesJsonDatabaseLog.escreverJson("INSERT", t);
        }
        return true;
    }

    public Boolean inserirOuAtualizar(T t) {
        operacoesJsonDatabaseLog.escreverJson("UPDATE", t);
        return true;
    }

    public Boolean inserirOuAtualizar(List<T> lista) {
        for(T t : lista) {
            operacoesJsonDatabaseLog.escreverJson("UPDATE", t);
        }
        return true;
    }

    public Boolean atualizar(T t, Object... chaves) {
        operacoesJsonDatabaseLog.escreverJson("UPDATE", t);
        return true;
    }

    public abstract Boolean deletar(Object... chaves);

    abstract void deletar(List<T> lista);

    abstract Cursor listarCursor();

    abstract List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     * @param ids Identificadores da entidade no banco de dados
     * @return Retorna o objeto encontrado com a ids ou nulo, se não encontrar
     */
    abstract T listarPorId(Object... ids);

    protected void escreveDatabaseLogFileDelete(T t) {
        operacoesJsonDatabaseLog.escreverJson("DELETE", t);
    }
}
