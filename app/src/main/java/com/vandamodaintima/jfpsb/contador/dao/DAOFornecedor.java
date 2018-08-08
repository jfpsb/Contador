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

public class DAOFornecedor extends DAO<Fornecedor> {
    public DAOFornecedor(SQLiteDatabase conn) {
        super(conn);
        TABELA = "fornecedor";
    }

    @Override
    public long inserir(Fornecedor objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", objeto.getCnpj());
            contentValues.put("nome", objeto.getNome());
            contentValues.put("fantasia", objeto.getFantasia());

            return conn.insert(TABELA, "", contentValues);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long atualizar(Fornecedor objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", objeto.getCnpj());
            contentValues.put("nome", objeto.getNome());
            contentValues.put("fantasia", objeto.getFantasia());

            return conn.update(TABELA, contentValues, "cnpj = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... chaves) {
        try {
            return conn.delete(TABELA, "cnpj = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Fornecedor.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}