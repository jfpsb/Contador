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

    public long[] inserir(Produto produto) {
        long[] result = new long[2];
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCod_barra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("fornecedor", produto.getFornecedor());

            result[0] = conn.insertOrThrow(TABELA, "", contentValues);
        } catch (SQLiteConstraintException sce) {
            Log.i("Contador", sce.getMessage());
            result[0] = -1;
            result[1] = TratamentoMensagensSQLite.retornaCodigoErro(sce.getMessage());
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
            result[0] = -1;
            result[1] = -1;
        }

        return result;
    }

    public void inserirVarios(TelaProduto telaProduto, Produto[] produtos) {
        for(int i = 0; i < produtos.length; i++) {
            Produto produto = produtos[i];

            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produto.getCod_barra());
            contentValues.put("descricao", produto.getDescricao());
            contentValues.put("preco", produto.getPreco());
            contentValues.put("fornecedor", produto.getFornecedor());

            Log.i("Contador", produto.getCod_barra());

            conn.insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

            telaProduto.runOnUiThread(TelaProduto.msgTxtProgressStatus("Produto " + (i + 1) + " cadastrado no banco de dados"));
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
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE fornecedor = cnpj OR fornecedor IS NULL", null);
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosDescricao(String descricao) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND descricao LIKE ?", new String[] {"%" + descricao +"%"});
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosCodBarra(String cod_barra) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND cod_barra LIKE ?", new String[] {"%" + cod_barra + "%" });
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar produtos: " + e.toString());
            return null;
        }
    }

    public Cursor selectProdutosFornecedor(String fornecedor_nome) {
        try {
            return conn.rawQuery("SELECT cod_barra as _id, fornecedor, nome, descricao, preco FROM produto LEFT JOIN fornecedor ON produto.fornecedor = fornecedor.cnpj WHERE (fornecedor = cnpj OR fornecedor IS NULL) AND nome LIKE ?", new String[] {"%"+fornecedor_nome+"%"});
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
