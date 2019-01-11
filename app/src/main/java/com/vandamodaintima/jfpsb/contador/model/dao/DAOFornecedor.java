package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

import java.util.ArrayList;

public class DAOFornecedor implements DAO<Fornecedor> {
    private static final String TABELA = "fornecedor";

    private SQLiteDatabase sqLiteDatabase;

    public DAOFornecedor(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public Boolean inserir(Fornecedor fornecedor) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", fornecedor.getCnpj());
            contentValues.put("nome", fornecedor.getNome());

            if (fornecedor.getFantasia() != null) {
                contentValues.put("fantasia", fornecedor.getFantasia());
            } else {
                contentValues.putNull("fantasia");
            }

            sqLiteDatabase.insertOrThrow(TABELA, null, contentValues);

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "Erro ao cadastrar fornecedor", ex);
        }

        return false;
    }

    @Override
    public Boolean atualizar(Fornecedor fornecedor, Object... chaves) {
        String cnpj = String.valueOf(chaves[0]);

        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", fornecedor.getNome());
        contentValues.put("fantasia", fornecedor.getFantasia());

        long result = sqLiteDatabase.update(TABELA, contentValues, "cnpj = ?", new String[]{cnpj});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        long result = sqLiteDatabase.delete(TABELA, "cnpj = ?", new String[]{String.valueOf(chaves[0])});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return sqLiteDatabase.query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public Fornecedor listarPorId(Object... ids) {
        Fornecedor fornecedor = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            fornecedor = new Fornecedor();
            cursor.moveToFirst();

            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
        }

        cursor.close();

        return fornecedor;
    }

    public Fornecedor listarPorIdOuNome(String termo) {
        Fornecedor fornecedor = null;

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cnpj = ? OR nome = ?", new String[]{termo, termo}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            fornecedor = new Fornecedor();

            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
        }

        cursor.close();

        return fornecedor;
    }

    public Cursor listarPorNomeCursor(String nome) {
        return sqLiteDatabase.query(TABELA, null, "nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, null, null);
    }

    public ArrayList<Fornecedor> listarPorNome(String nome) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor cursor = listarPorNomeCursor(nome);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));

                fornecedores.add(fornecedor);
            }
        }

        cursor.close();

        return fornecedores;
    }

    public ArrayList<Fornecedor> listarPorFantasia(String fantasia) {
        return null;
    }

    public Cursor listarPorCnpjNomeFantasiaCursor(String termo) {
        return sqLiteDatabase.query(TABELA, Fornecedor.getColunas(), "cnpj LIKE ? OR nome LIKE ? OR fantasia LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<Fornecedor> listarPorCnpjNomeFantasia(String termo) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor cursor = listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));

                fornecedores.add(fornecedor);
            }
        }

        cursor.close();

        return fornecedores;
    }
}