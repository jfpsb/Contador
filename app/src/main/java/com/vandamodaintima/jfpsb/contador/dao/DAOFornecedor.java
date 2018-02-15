package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOFornecedor {
    private SQLiteDatabase conn;
    private final String TABELA = "fornecedor";

    public DAOFornecedor(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long inserir(Fornecedor fornecedor) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", fornecedor.getNome());
        contentValues.put("cnpj", fornecedor.getCnpj());

        return conn.insert(TABELA, "", contentValues);
    }

    public Fornecedor selectFornecedor(int id) {
        Cursor c = conn.query(true, TABELA, null, "cnpj = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Fornecedor fornecedor = new Fornecedor();

            fornecedor.setCnpj(c.getString(0));
            fornecedor.setNome(c.getString(1));

            return fornecedor;
        }

        return null;
    }

    public Cursor selectFornecedores() {
        try {
            return conn.query(TABELA, new String[] {"cnpj _id", "nome"}, null, null, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar fornecedores: " + e.toString());
            return null;
        }
    }

    public Cursor selectFornecedores(String termo) {
        try {
            return conn.query(TABELA, new String[] {"cnpj _id", "nome"}, "nome LIKE ? OR cnpj LIKE ?", new String[] { "%" + termo + "%", "%" + termo + "%"}, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar fornecedores: " + e.toString());
            return null;
        }
    }

    public int deletar(String id) {
        int result = conn.delete(TABELA, "cnpj = ?", new String[] { id });
        Log.i("Contador", "Deletando fornecedor com cnpj " + id);
        return result;
    }

    public int atualizar(Fornecedor fornecedor) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("cnpj", fornecedor.getCnpj());
        contentValues.put("nome", fornecedor.getNome());

        return conn.update(TABELA, contentValues, "cnpj = ?", new String[] {fornecedor.getCnpj()});
    }
}
