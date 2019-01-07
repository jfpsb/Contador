package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Loja;

import java.util.ArrayList;

public class DAOLoja implements DAO<Loja> {
    private static final String TABELA = "loja";

    private SQLiteDatabase sqLiteDatabase;

    public DAOLoja(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public Boolean inserir(Loja loja) {
        try {
            sqLiteDatabase.beginTransaction();

            ContentValues contentLoja = new ContentValues();

            contentLoja.put("cnpj", loja.getCnpj());
            contentLoja.put("nome", loja.getNome());

            if (loja.getMatriz() == null) {
                contentLoja.putNull("matriz");
            } else {
                contentLoja.put("matriz", loja.getMatriz().getCnpj());
            }

            sqLiteDatabase.insertOrThrow(TABELA, "", contentLoja);
            sqLiteDatabase.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(Loja loja, Object... chaves) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", loja.getNome());

        int result = sqLiteDatabase.update(TABELA, contentValues, "cnpj = ?", new String[]{String.valueOf(chaves[0])});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        int result = sqLiteDatabase.delete(TABELA, "cnpj = ?", new String[]{String.valueOf(chaves[0])});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return sqLiteDatabase.query(TABELA, Loja.getColunas(), null, null, null, null, null);
    }

    @Override
    public Loja listarPorId(Object... ids) {
        Loja loja = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            loja = new Loja();

            loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));
        }

        return loja;
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return sqLiteDatabase.query(TABELA, Loja.getColunas(), "cnpj LIKE ? OR nome LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<Loja> listarPorNomeCnpj(String termo) {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));

                lojas.add(loja);
            }
        }

        cursor.close();

        return lojas;
    }

    public ArrayList<Loja> listarMatrizes() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "matriz IS NULL", null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                lojas.add(loja);
            }
        }

        cursor.close();

        return lojas;
    }
}