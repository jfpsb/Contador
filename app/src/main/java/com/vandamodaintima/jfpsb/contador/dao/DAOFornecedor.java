package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOFornecedor {
    private SQLiteDatabase conn;
    private final String TABELA = "fornecedor";

    public DAOFornecedor(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long[] inserir(Fornecedor fornecedor) {
        long[]  result = new long[2];
        try {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", fornecedor.getNome());
        contentValues.put("cnpj", fornecedor.getCnpj());

        result[0] = conn.insertOrThrow(TABELA, "", contentValues);
        } catch (SQLiteConstraintException sce) {
            Log.i("Contador", sce.getMessage());
            result[0] = -1;
            result[1] = TratamentoMensagensSQLite.retornaCodigoErro(sce.getMessage());
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            result[0] = -1;
            result[1] = -1;
        }

        return result;
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
            return conn.query(TABELA, new String[] {"cnpj as _id", "nome"}, null, null, null, null, null);
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
