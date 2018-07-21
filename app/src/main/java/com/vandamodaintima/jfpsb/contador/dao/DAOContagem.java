package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

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
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", contagem.getLoja().getIdloja());
            contentValues.put("datainicio", TrataDisplayData.getDataEmString(contagem.getDatainicio()));

            long result = conn.insertOrThrow(TABELA, "", contentValues);

            return result;
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
        try {
            return conn.query(TABELA, Contagem.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return null;
    }

    public Cursor selectRaw(String sql, String[] selection) {
        try {
            return conn.rawQuery(sql, selection);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return null;
    }

    public int deletar(int id) {
        try {
            return conn.delete(TABELA, "idcontagem = ?", new String[]{String.valueOf(id)});
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    public int atualizar(Contagem contagem, int chave) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("idcontagem", contagem.getIdcontagem());
            contentValues.put("loja", contagem.getLoja().getIdloja());
            contentValues.put("datainicio", TrataDisplayData.getDataEmString(contagem.getDatainicio()));
            contentValues.put("datafinal", TrataDisplayData.getDataEmString(contagem.getDatafinal()));

            return conn.update(TABELA, contentValues, "idcontagem = ?", new String[]{String.valueOf(chave)});
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    public int atualizarSemDataFinal(Contagem contagem, int chave) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("idcontagem", contagem.getIdcontagem());
            contentValues.put("loja", contagem.getLoja().getIdloja());
            contentValues.put("datainicio", TrataDisplayData.getDataEmString(contagem.getDatainicio()));

            return conn.update(TABELA, contentValues, "idcontagem = ?", new String[]{String.valueOf(chave)});
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }
}
