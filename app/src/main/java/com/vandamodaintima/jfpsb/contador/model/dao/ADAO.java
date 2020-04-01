package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.sincronizacao.DBLog;
import com.vandamodaintima.jfpsb.contador.sincronizacao.Sincronizacao;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public abstract class ADAO<T extends IModel & Serializable> {
    protected String TABELA;
    protected ConexaoBanco conexaoBanco;

    ADAO(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public Boolean inserir(T t) {
        Sincronizacao.addTransientLog(t, "INSERIR");
        Sincronizacao.writeLog();
        Sincronizacao.sendLog();
        return true;
    }

    public Boolean inserir(List<T> lista) {
        for (T t : lista)
            Sincronizacao.addTransientLog(t, "INSERIR");

        Sincronizacao.writeLog();
        Sincronizacao.sendLog();

        return true;
    }

    public Boolean atualizar(T t, Object... chaves) {
        Sincronizacao.addTransientLog(t, "UPDATE");
        Sincronizacao.writeLog();
        Sincronizacao.sendLog();
        return true;
    }

    public void replicate(T entity) {
        T t = listarPorId(entity.getIdentifier());

        if (t == null) {
            inserir(entity);
        } else {
            atualizar(entity, entity.getIdentifier());
        }

        Sincronizacao.writeLog();
    }

    public Boolean deletar(T objeto) {
        Object key = objeto.getIdentifier();

        if (!(key instanceof String[])) {
            key = new String[]{String.valueOf(key)};
        }

        long result = conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);

        if (result > 0) {
            Sincronizacao.addTransientLog(objeto, "DELETE");
            Sincronizacao.writeLog();
            Sincronizacao.sendLog();
        }

        return result > 0;
    }

    public void deletarLista(List<T> lista) {
        if (lista.size() == 0)
            return;

        for (T objeto : lista) {
            Object key = objeto.getIdentifier();

            if (!(key instanceof String[])) {
                key = new String[]{String.valueOf(key)};
            }

            long result = conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);

            if (result > 0) {
                Sincronizacao.addTransientLog(objeto, "DELETE");
            }
        }

        Sincronizacao.writeLog();
        Sincronizacao.sendLog();
    }

    public abstract Cursor listarCursor();

    public abstract List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     *
     * @param ids Identificadores da entidade no banco de dados
     * @return Retorna o objeto encontrado com a ids ou nulo, se não encontrar
     */
    public abstract T listarPorId(Object... ids);

    public abstract int getMaxId();
}
