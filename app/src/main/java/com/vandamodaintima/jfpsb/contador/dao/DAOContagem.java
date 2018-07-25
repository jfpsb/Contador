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

public class DAOContagem extends DAO<Contagem>{
    public DAOContagem(SQLiteDatabase conn) {
        super(conn);
        TABELA = "contagem";
    }

    @Override
    public long inserir(Contagem objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", objeto.getLoja().getCnpj());
            contentValues.put("datainicio", TrataDisplayData.getDataEmString(objeto.getDatainicio()));

            long result = conn.insertOrThrow(TABELA, "", contentValues);

            return result;
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long atualizar(Contagem objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("idcontagem", objeto.getIdcontagem());
            contentValues.put("loja", objeto.getLoja().getCnpj());
            contentValues.put("datainicio", TrataDisplayData.getDataEmString(objeto.getDatainicio()));

            if(objeto.getDatafinal() != null) {
                contentValues.put("datafinal", TrataDisplayData.getDataEmString(objeto.getDatafinal()));
            }
            else {
                contentValues.putNull("datafinal");
            }

            return conn.update(TABELA, contentValues, "idcontagem = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... id) {
        try {
            return conn.delete(TABELA, "idcontagem = ?", new String[]{String.valueOf(id[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Contagem.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
