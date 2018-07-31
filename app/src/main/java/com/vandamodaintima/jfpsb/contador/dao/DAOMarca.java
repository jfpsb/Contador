package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

public class DAOMarca extends DAO<Marca> {

    public DAOMarca(SQLiteDatabase conn) {
        super(conn);
        TABELA = "marca";
    }

    @Override
    public long inserir(Marca objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", objeto.getNome());

            return conn.insertOrThrow(TABELA, "", contentValues);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long atualizar(Marca objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", objeto.getNome());

            return conn.update(TABELA, contentValues, "id = ?", new String[] { String.valueOf(chaves[0]) });
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... id) {
        try {
            return conn.delete(TABELA, "id = ?", new String[]{String.valueOf(id[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Marca.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
