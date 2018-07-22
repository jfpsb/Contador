package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.ContagemProduto;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOContagemProduto extends DAO<ContagemProduto>{
    public DAOContagemProduto(SQLiteDatabase conn) {
        super(conn);
        TABELA = "contagem_produto";
    }

    @Override
    public long inserir(ContagemProduto objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("contagem", objeto.getContagem().getIdcontagem());
            contentValues.put("produto", objeto.getProduto().getCod_barra());
            contentValues.put("quant", objeto.getQuant());

            return conn.insertOrThrow(TABELA, "", contentValues);
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    @Override
    public long atualizar(ContagemProduto objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("id", objeto.getId());
            contentValues.put("contagem", objeto.getContagem().getIdcontagem());
            contentValues.put("produto", objeto.getProduto().getCod_barra());
            contentValues.put("quant", objeto.getQuant());

            return conn.update(TABELA, contentValues, "id = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    @Override
    public long deletar(Object... id) {
        try {
            return conn.delete(TABELA, "id = ?", new String[]{String.valueOf(id[0])});
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, ContagemProduto.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return null;
    }

    @Override
    public Cursor selectRaw(String sql, String[] selection) {
        try {
            return conn.rawQuery(sql, selection);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return null;
    }
}
