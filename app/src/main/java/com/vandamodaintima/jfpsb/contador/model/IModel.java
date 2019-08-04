package com.vandamodaintima.jfpsb.contador.model;

import android.database.Cursor;

import java.util.ArrayList;

public interface IModel<T> {
    String LOG = "Contador";

    Boolean inserir();
    Boolean atualizar();
    Boolean deletar();
    Cursor listarCursor();
    ArrayList<T> listar();
    T listarPorId(Object... ids);
}
