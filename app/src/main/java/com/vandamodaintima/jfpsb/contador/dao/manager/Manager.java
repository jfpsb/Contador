package com.vandamodaintima.jfpsb.contador.dao.manager;

public interface Manager<T> {
    public boolean inserir(T objeto);
    public boolean deletar(T objeto);

}
