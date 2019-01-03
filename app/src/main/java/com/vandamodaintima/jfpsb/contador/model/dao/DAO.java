package com.vandamodaintima.jfpsb.contador.model.dao;

import java.util.ArrayList;

public interface DAO<T> {

    String LOG = "Contador";

    Boolean inserir(T t);

    Boolean atualizar(T t, Object... chaves);

    Boolean deletar(Object... chaves);

    ArrayList<T> listar();

    ArrayList<T> listar(String selection, String[] args);

    T listarPorId(Object... ids);
}