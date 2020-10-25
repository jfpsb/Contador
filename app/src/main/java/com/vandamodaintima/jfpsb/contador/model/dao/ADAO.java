package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.IModel;

import java.io.Serializable;
import java.util.List;

public abstract class ADAO<T extends IModel<T> & Serializable> {
    protected String TABELA;
    protected ConexaoBanco conexaoBanco;

    ADAO(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public abstract Boolean inserir(T t);

    public abstract Boolean inserir(List<T> lista);

    public abstract Boolean atualizar(T t);

    public Boolean deletar(T objeto) {
        Object key = objeto.getIdentifier();

        if (!(key instanceof String[])) {
            key = new String[]{String.valueOf(key)};
        }

        long result = conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);

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
            conexaoBanco.conexao().delete(TABELA, objeto.getDeleteWhereClause(), (String[]) key);
        }
    }

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
