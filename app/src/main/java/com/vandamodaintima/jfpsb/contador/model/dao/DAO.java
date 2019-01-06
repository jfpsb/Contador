package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

public interface DAO<T> {

    String LOG = "Contador";

    Boolean inserir(T t);

    Boolean atualizar(T t, Object... chaves);

    Boolean deletar(Object... chaves);

    Cursor listarCursor();

    T listarPorId(Object... ids);
}