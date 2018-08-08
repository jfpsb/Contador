package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.dao.DAO;

import java.util.ArrayList;

public abstract class Manager<T> {
    protected DAO daoEntidade;

    public boolean inserir(T obj) {
        long result = daoEntidade.inserir(obj);

        if(result != -1)
            return true;

        return false;
    }

    public boolean atualizar(T obj, Object... chaves) {
        long result = daoEntidade.atualizar(obj, chaves);

        if(result != -1)
            return true;

        return false;
    }

    public boolean deletar(Object... chaves) {
        long result = daoEntidade.deletar(chaves);

        if(result != -1) {
            return true;
        }

        return false;
    }
    public abstract ArrayList<T> listar();
    public abstract Cursor listarCursor();
    public abstract T listarPorChave(Object... chaves);
    public abstract Cursor listarCursorPorChave(Object... chaves);
}
