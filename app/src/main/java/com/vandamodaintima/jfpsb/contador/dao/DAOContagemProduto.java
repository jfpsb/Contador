package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem_Produto;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class DAOContagemProduto {
    private SQLiteDatabase conn;
    private final String TABELA = "contagem_produto";

    public DAOContagemProduto(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long inserir(Contagem_Produto contagem_produto) {
        try {
        ContentValues contentValues = new ContentValues();

        contentValues.put("contagem", contagem_produto.getContagem());
        contentValues.put("produto", contagem_produto.getProduto());
        contentValues.put("quant", contagem_produto.getQuant());

        long result = conn.insertOrThrow(TABELA, "", contentValues);

        return result;
        } catch (SQLiteConstraintException sce) {
            Log.i("Contador", sce.getMessage());
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
        }

        return -1;
    }

    public Contagem_Produto selectContagemProduto(int contagem, int produto) {
        Cursor c = conn.rawQuery("SELECT id as _id, contagem, produto, descricao, quant FROM contagem_produto, produto WHERE produto = cod_barra AND contagem = ? AND produto = ? ORDER BY descricao", new String[] {String.valueOf(contagem), String.valueOf(produto)});

        if(c.getCount() > 0) {
            c.moveToFirst();

            Contagem_Produto contagem_produto = new Contagem_Produto();

            contagem_produto.setId(c.getInt(0));
            contagem_produto.setContagem(c.getInt(1));
            contagem_produto.setProduto(c.getString(2));
            contagem_produto.setQuant(c.getInt(3));

            return contagem_produto;
        }

        return null;
    }

    public Cursor selectContagemProdutos() {
        try {
            return conn.rawQuery("SELECT id as _id, contagem, produto, fornecedor, descricao, SUM(quant) as quant FROM contagem_produto, produto WHERE produto = cod_barra GROUP BY produto ORDER BY descricao", null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar contagem de produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectContagemProdutos(int contagem) {
        try {
            return conn.rawQuery("SELECT id as _id, contagem, produto, descricao, fornecedor, SUM(quant) as quant FROM contagem_produto, produto WHERE produto = cod_barra AND contagem = ? GROUP BY produto ORDER BY descricao", new String[] { String.valueOf(contagem) });
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar contagem de produtos: " + e.toString());
            return null;
        }
    }

    public int deletar(int id) {
        int result = conn.delete(TABELA, "id = ?", new String[] {String.valueOf(id)});
        return result;
    }
}
