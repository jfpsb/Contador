package com.vandamodaintima.jfpsb.contador.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class DAO<T> {
    protected SQLiteDatabase conn;
    protected String TABELA;

    public DAO(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public abstract long inserir(T objeto);
    public abstract long atualizar(T objeto, Object... chaves);
    public abstract long deletar(Object... chaves);
    public abstract Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);

    public Cursor selectRaw(String sql, String[] selection) {
        try {
            return conn.rawQuery(sql, selection);
        }
        catch (Exception e) {
            Log.e("Contador", "Erro em SelectRaw", e);
        }

        return null;
    }
}
