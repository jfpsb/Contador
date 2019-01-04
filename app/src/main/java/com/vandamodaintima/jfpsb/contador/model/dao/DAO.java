package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

public interface DAO<T> {

    String LOG = "Contador";

    Boolean inserir(T t);

    Boolean atualizar(T t, Object... chaves);

    Boolean deletar(Object... chaves);

    Cursor listar(String selection, String[] args, String groupBy, String having, String orderBy, String limit);

    T listarPorId(Object... ids);
}