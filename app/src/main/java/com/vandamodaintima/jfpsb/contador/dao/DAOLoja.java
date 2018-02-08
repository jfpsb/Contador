package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Loja;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOLoja {
    private SQLiteDatabase conn;
    private final String TABELA = "loja";

    public DAOLoja(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long inserir(Loja loja) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", loja.getNome());

        long id = conn.insert(TABELA, "", contentValues);

        return id;
    }

    public Loja selectLoja(int id) {
        Cursor c = conn.query(true, TABELA, null, "idloja = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Loja loja = new Loja();

            loja.setIdloja(c.getInt(0));
            loja.setNome(c.getString(1));

            return loja;
        }

        return null;
    }

    public Cursor selectLojas() {
        try {
            return conn.query(TABELA, null, null, null, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar lojas: " + e.toString());
            return null;
        }
    }
}
