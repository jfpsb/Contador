package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOContagem {
    private SQLiteDatabase conn;
    private final String TABELA = "contagem";

    public DAOContagem(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long inserir(Contagem contagem) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("loja", contagem.getLoja());
        contentValues.put("datainicio", contagem.getDatainicio());

        return conn.insert(TABELA, "", contentValues);
    }

    public Contagem selectContagem(int id) throws ParseException {
        Cursor c = conn.query(true, TABELA, new String[] {"idcontagem _id", "loja", "datainicio", "datafinal"}, "idcontagem = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Contagem contagem = new Contagem();

            contagem.setIdcontagem(c.getInt(0));
            contagem.setLoja(c.getInt(1));
            contagem.setDatainicio(c.getString(2));
            contagem.setDatainicio(c.getString(3));

            return contagem;
        }

        return null;
    }

    public Cursor selectContagens() {
        try {
            return conn.query(TABELA, new String[] {"idcontagem _id", "loja", "datainicio", "datafinal"}, null, null, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar contagens: " + e.toString());
            return null;
        }
    }

    public Cursor selectContagens(String data1, String data2, String loja) {
        try {
            return conn.query(TABELA, new String[] {"idcontagem _id", "loja", "datainicio", "datafinal"}, "datainicio BETWEEN ? AND ? AND loja = ?", new String[] { data1, data2, loja }, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar contagens: " + e.toString());
            return null;
        }
    }

    public int deletar(int id) {
        int result = conn.delete(TABELA, "idcontagem = " + String.valueOf(id), null);
        Log.i("Contador", "Deletando contagem com id " + id);
        return result;
    }
}
