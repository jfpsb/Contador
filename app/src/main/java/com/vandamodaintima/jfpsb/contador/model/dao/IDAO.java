package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import java.util.List;

public interface IDAO<T> {
    String LOG = "Contador";

    Boolean inserir(T t);

    Boolean inserir(List<T> lista);

    Boolean inserirOuAtualizar(T t);

    Boolean inserirOuAtualizar(List<T> lista);

    Boolean atualizar(T t, Object... chaves);

    Boolean deletar(Object... chaves);

    void deletar(List<T> lista);

    Cursor listarCursor();

    List<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     * @param ids Identificadores da entidade no banco de dados
     * @return Retorna o objeto encontrado com a ids ou nulo, se não encontrar
     */
    T listarPorId(Object... ids);
}
