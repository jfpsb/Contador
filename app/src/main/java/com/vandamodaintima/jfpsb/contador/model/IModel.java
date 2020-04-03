package com.vandamodaintima.jfpsb.contador.model;

import java.util.List;

public interface IModel<T> {
    /**
     * Retorna a chave do modelo
     * @return Chave do modelo. Caso possua mais de uma chave, retorna um array de String com as chaves
     */
    Object getIdentifier();

    /**
     * Retorna os argumentos usados no where para deletar este modelo
     * @return
     */
    String getDeleteWhereClause();

    Boolean salvar();

    Boolean salvar(List<T> lista);

    Boolean atualizar();

    Boolean deletar();

    List<T> listar();

    T listarPorId(Object... ids);

    void load(Object... ids);
}
