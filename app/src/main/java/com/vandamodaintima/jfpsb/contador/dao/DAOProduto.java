package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

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

    public Produto selectProduto(int id) {
        Cursor c = conn.query(true, TABELA, Produto.getProdutoColunas(), "cob_barra = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Produto produto = new Produto();

            produto.setCod_barra(c.getInt(0));
            produto.setFornecedor(c.getString(1));
            produto.setDescricao(c.getString(2));
            produto.setPreco(c.getDouble(3));

            return produto;
        }

        return null;
    }

    public Cursor selectProdutos() {
        try {
            return conn.query(TABELA, Produto.getProdutoColunas(), null, null, null, null, null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosDescricao(String descricao) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, cnpj, nome, descricao, preco FROM produto, fornecedor WHERE descricao LIKE ? AND fornecedor = cnpj", new String[] {"%" + descricao +"%"});
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosCodBarra(String cod_barra) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, cnpj, nome, descricao, preco FROM produto, fornecedor WHERE CAST(cod_barra as TEXT) LIKE ? AND fornecedor = cnpj", new String[] {"%" + cod_barra + "%" });
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosFornecedor(String fornecedor_nome) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, cnpj, nome, descricao, preco FROM produto, fornecedor WHERE nome LIKE ? AND fornecedor = cnpj", new String[] {"%"+fornecedor_nome+"%"});
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public int deletar(int id) {
        int result = conn.delete(TABELA, "cod_barra = " + String.valueOf(id), null);
        Log.i("Contador", "Deletando produto com código de barra " + id);
        return result;
    }
}
