package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Marca;

import java.util.ArrayList;

public class DAOMarca implements DAO<Marca> {
    private static final String TABELA = "marca";

    private SQLiteDatabase sqLiteDatabase;

    public DAOMarca(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public Boolean inserir(Marca marca) {
        return null;
    }

    @Override
    public Boolean atualizar(Marca marca, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public ArrayList<Marca> listar() {
        return null;
    }

    @Override
    public Marca listarPorId(Object... ids) {
        Marca marca = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "id = ?", new String[] { String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            marca = new Marca();
            cursor.moveToFirst();

            marca.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();

        return marca;
    }

    public ArrayList<Marca> listarPorNome(String nome) {
        return null;
    }
}