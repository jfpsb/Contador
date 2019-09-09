package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import java.util.List;

public interface IDAO<T> {
    String LOG = "Contador";

    Boolean inserir(T t);

    Boolean inserir(List<T> lista);

    Boolean atualizar(T t, Object... chaves);

    Boolean deletar(Object... chaves);

    Cursor listarCursor();

    List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     * @param ids
     * @return
     */
    T listarPorId(Object... ids);
}
