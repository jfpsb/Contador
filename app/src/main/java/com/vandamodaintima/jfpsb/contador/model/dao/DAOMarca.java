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
    public Cursor listar() {
        return sqLiteDatabase.query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public Marca listarPorId(Object... ids) {
        Marca marca = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, Marca.getColunas(), "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            marca = new Marca();
            cursor.moveToFirst();

            marca.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
            marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();

        return marca;
    }

    public Cursor listarPorNomeCursor(String nome) {
        return sqLiteDatabase.query(TABELA, Marca.getColunas(), "nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, null, null);
    }

    public ArrayList<Marca> listarPorNome(String nome) {
        ArrayList<Marca> marcas = new ArrayList<>();

        Cursor cursor = listarPorNomeCursor(nome);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Marca marca = new Marca();

                marca.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                marcas.add(marca);
            }
        }

        cursor.close();

        return marcas;
    }
}