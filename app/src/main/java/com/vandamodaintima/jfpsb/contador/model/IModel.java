package com.vandamodaintima.jfpsb.contador.model;

public interface IModel {
    /**
     * Retorna a chave do modelo
     * @return Chave do modelo. Caso possua mais de uma chave, retorna um array de String com as chaves
     */
    Object getIdentifier();

    /**
     * Retorna identificador que será utilizado para compôr o nome do Log desse modelo
     * @return Chave primária ou composição de chaves primárias do modelo
     */
    String getDatabaseLogIdentifier();

    /**
     * Retorna os argumentos usados no where para deletar este modelo
     * @return
     */
    String getDeleteWhereClause();
}
