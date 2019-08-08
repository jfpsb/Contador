package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import java.util.ArrayList;

public interface IModel<T> {
    String LOG = "Contador";

    Boolean inserir();
    Boolean inserir(ArrayList<T> lista);
    Boolean atualizar();
    Boolean deletar();
    Cursor listarCursor();
    ArrayList<T> listar();

    /**
     * Retorna uma nova instância de uma entidade usando a(s) id(s)
     * @param ids
     * @return
     */
    T listarPorId(Object... ids);

    /**
     * Carrega a entidade encontrada no modelo
     * @param ids
     */
    void load(Object... ids);
}
