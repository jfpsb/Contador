package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.TelaProduto;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOProduto {
    private SQLiteDatabase conn;
    private final String TABELA = "produto";

    public DAOProduto(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long inserir(Produto produto) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("cod_barra", produto.getCod_barra());
        contentValues.put("descricao", produto.getDescricao());
        contentValues.put("preco", produto.getPreco());
        contentValues.put("fornecedor", produto.getFornecedor());

        return conn.insert(TABELA, "", contentValues);
    }

    public long inserirBulk(Produto produto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCod_barra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("fornecedor", produto.getFornecedor());

            return conn.insertWithOnConflict(TABELA, "", contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLException e) {
            Log.i("Contador", e.getMessage());
            return -1;
        }
    }

    public Produto selectProduto(int id) {
        Cursor c = conn.query(true, TABELA, Produto.getProdutoColunas(), "cob_barra = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Produto produto = new Produto();

            produto.setCod_barra(c.getString(0));
            produto.setFornecedor(c.getString(1));
            produto.setDescricao(c.getString(2));
            produto.setPreco(c.getDouble(3));

            return produto;
        }

        return null;
    }

    public Cursor selectProdutos() {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.id WHERE fornecedor = id OR fornecedor IS NULL ORDER BY descricao", null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosDescricao(String descricao) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.id WHERE (fornecedor = id OR fornecedor IS NULL) AND descricao LIKE ? ORDER BY descricao", new String[] {"%" + descricao +"%"});
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosCodBarra(String cod_barra) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.id WHERE (fornecedor = id OR fornecedor IS NULL) AND cod_barra LIKE ? ORDER BY descricao", new String[] {"%" + cod_barra + "%" });
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosFornecedor(String fornecedor_nome) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.id WHERE (fornecedor = id OR fornecedor IS NULL) AND nome LIKE ? ORDER BY descricao", new String[] {"%"+fornecedor_nome+"%"});
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public int deletar(String id) {
        int result = conn.delete(TABELA, "cod_barra = " + String.valueOf(id), null);
        Log.i("Contador", "Deletando produto com código de barra " + id);
        return result;
    }

    public int atualizar(Produto produto) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("cod_barra", produto.getCod_barra());
        contentValues.put("descricao", produto.getDescricao());
        contentValues.put("fornecedor", produto.getFornecedor());
        contentValues.put("preco", produto.getPreco());

        return conn.update(TABELA, contentValues, "cod_barra = ?", new String[] {produto.getCod_barra()});
    }
}
