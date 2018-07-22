package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOLoja extends DAO<Loja> {
    public DAOLoja(SQLiteDatabase conn) {
        super(conn);
        TABELA = "loja";
    }

    @Override
    public long inserir(Loja objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", objeto.getCnpj());
            contentValues.put("nome", objeto.getNome());

            return conn.insert(TABELA, "", contentValues);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long atualizar(Loja objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", objeto.getCnpj());
            contentValues.put("nome", objeto.getNome());

            return conn.update(TABELA, contentValues, "cnpj = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... id) {
        try {
            return conn.delete(TABELA, "cnpj = ?", new String[]{String.valueOf(id[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Loja.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
