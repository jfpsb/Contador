package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Produto;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOProduto extends DAO<Produto> {
    public DAOProduto(SQLiteDatabase conn) {
        super(conn);
        TABELA = "produto";
    }

    @Override
    public long inserir(Produto objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", objeto.getCod_barra());
            contentValues.put("descricao", objeto.getDescricao());
            contentValues.put("preco", objeto.getPreco());
            contentValues.put("fornecedor", objeto.getFornecedor().getId());

            return conn.insert(TABELA, "", contentValues);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    public long inserirBulk(Produto produto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCod_barra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("fornecedor", produto.getFornecedor().getId());

            return conn.insertWithOnConflict(TABELA, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLException e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long atualizar(Produto objeto, Object... chaves) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", objeto.getCod_barra());
            contentValues.put("fornecedor", objeto.getFornecedor().getId());
            contentValues.put("descricao", objeto.getDescricao());
            contentValues.put("preco", objeto.getPreco());

            return conn.update(TABELA, contentValues, "cod_barra = ?", new String[]{String.valueOf(chaves[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... id) {
        try {
            return conn.delete(TABELA, "cod_barra = ?", new String[]{String.valueOf(id[0])});
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Produto.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
