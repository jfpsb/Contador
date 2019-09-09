package com.vandamodaintima.jfpsb.contador.model.manager;

import java.util.List;

public interface IManager<T> {
    void resetaModelo();

    Boolean salvar();

    Boolean salvar(List<T> lista);

    Boolean atualizar(Object... ids);

    Boolean deletar();

    List<T> listar();

    T listarPorId(Object... ids);

    void load(Object... ids);
}
