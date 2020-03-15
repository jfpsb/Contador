package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.sincronizacao.DatabaseLogFile;
import com.vandamodaintima.jfpsb.contador.sincronizacao.Sincronizacao;
import com.vandamodaintima.jfpsb.contador.sincronizacao.ZonedDateTimeGsonAdapter;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class ADAO<T extends IModel> {
    protected String TABELA;
    protected ConexaoBanco conexaoBanco;

    ADAO(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public Boolean inserir(T t, boolean writeToJson, boolean sendToServer) throws IOException {
        if (writeToJson) {
            DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("INSERT", t);
            if(sendToServer)
                Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
        }
        return true;
    }

    public Boolean inserir(List<T> lista, boolean writeToJson, boolean sendToServer) throws IOException {
        if (writeToJson) {
            for (T t : lista) {
                DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("INSERT", t);
                if(sendToServer)
                    Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
            }
        }
        return true;
    }

    public Boolean inserirOuAtualizar(T t, boolean writeToJson, boolean sendToServer) throws IOException {
        if (writeToJson) {
            DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("UPDATE", t);
            if(sendToServer)
                Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
        }
        return true;
    }

    public Boolean inserirOuAtualizar(List<T> lista, boolean writeToJson, boolean sendToServer) throws IOException {
        if (writeToJson) {
            for (T t : lista) {
                DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("INSERT", t);
                if(sendToServer)
                    Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
            }
        }
        return true;
    }

    public Boolean atualizar(T t, boolean writeToJson, boolean sendToServer, Object... chaves) throws IOException {
        if (writeToJson) {
            DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("UPDATE", t);
            if(sendToServer)
                Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
        }
        return true;
    }

    public Boolean deletar(T objeto, boolean writeToJson, boolean sendToServer) {
        Object key = objeto.getIdentifier();

        if (!(key instanceof String[])) {
            key = new String[]{String.valueOf(key)};
        }

        long result = conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);

        if (result > 0 && writeToJson) {
            try {
                DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("DELETE", objeto);
                if(sendToServer)
                    Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
            } catch (IOException io) {
                Log.e(ActivityBaseView.LOG, "Erro ao Escrever Log em DELETE. Inserindo Novamente em Banco de Dados: " + io.getMessage(), io);
                try {
                    inserir(objeto, true, true);
                } catch (IOException e) {
                    Log.e(ActivityBaseView.LOG, "Erro ao Inserir Log Novamente: " + e.getMessage(), e);
                }
            }
        }

        return result > 0;
    }

    public void deletarLista(List<T> lista, boolean writeToJson, boolean sendToServer) {

        for (T objeto : lista) {
            Object key = objeto.getIdentifier();

            if (!(key instanceof String[])) {
                key = new String[]{String.valueOf(key)};
            }

            long result = conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);

            if (result > 0 && writeToJson) {
                try {
                    DatabaseLogFile<T> databaseLogFile = Sincronizacao.escreverJson("DELETE", objeto);
                    if(sendToServer)
                        Sincronizacao.sendDatabaseLogFileToServer(databaseLogFile);
                } catch (IOException io) {
                    Log.e(ActivityBaseView.LOG, "Erro ao Escrever Log em DELETE. Inserindo Novamente em Banco de Dados: " + io.getMessage(), io);
                    try {
                        inserir(objeto, true, true);
                    } catch (IOException e) {
                        Log.e(ActivityBaseView.LOG, "Erro ao Inserir Log Novamente: " + e.getMessage(), e);
                    }
                }
            }
        }
    }

    public abstract Cursor listarCursor();

    public abstract List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     *
     * @param ids Identificadores da entidade no banco de dados
     * @return Retorna o objeto encontrado com a ids ou nulo, se não encontrar
     */
    abstract T listarPorId(Object... ids);
}
