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
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public ArrayList<Fornecedor> listar() {
        return null;
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

    public ArrayList<Fornecedor> listarPorNome(String nome) {
        return null;
    }

    public ArrayList<Fornecedor> listarPorFantasia(String fantasia) {
        return null;
    }

    public ArrayList<Fornecedor> listarPorCnpjNomeFantasia(String termo) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "cnpj LIKE ? OR nome LIKE ? OR nome_fantasia LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);

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