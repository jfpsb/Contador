package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.produto.TelaProduto;

import java.util.ArrayList;

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
            conn.beginTransaction();

            ContentValues contentValues = getContentValues(objeto);

            conn.insertOrThrow(TABELA, null, contentValues);

            for (int i = 0; i < objeto.getCod_barra_fornecedor().size(); i++) {
                CodBarraFornecedor codigo = objeto.getCod_barra_fornecedor().get(i);

                ContentValues content = new ContentValues();

                content.put("produto", objeto.getCod_barra());
                content.put("codigo", codigo.getCodigo());

                conn.insertOrThrow("cod_barra_fornecedor", null, content);
            }

            conn.setTransactionSuccessful();

            return 1;
        } catch (Exception e) {
            Log.e(ActivityBase.LOG, e.getMessage(), e);
        } finally {
            conn.endTransaction();
        }

        return -1;
    }

    public long inserirVarios(ArrayList<Produto> produtos, TelaProduto.Tarefa.Progresso progresso) {
        try {
            if (produtos.size() == 0)
                throw new Exception("Não Há Produtos");

            conn.beginTransaction();

            for (Produto produto : produtos) {
                ContentValues contentValues = getContentValues(produto);

                conn.insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                for (CodBarraFornecedor codigo : produto.getCod_barra_fornecedor()) {
                    ContentValues contentCodBarraFornecedor = new ContentValues();

                    contentCodBarraFornecedor.put("produto", produto.getCod_barra());
                    contentCodBarraFornecedor.put("codigo", codigo.getCodigo());

                    conn.insertWithOnConflict("cod_barra_fornecedor", null, contentCodBarraFornecedor, SQLiteDatabase.CONFLICT_IGNORE);
                }

                progresso.publish("Produto " + produto.getCod_barra() + " - " + produto.getDescricao() + " Cadastrado");
            }

            conn.setTransactionSuccessful();

            return 1;
        } catch (SQLException e) {
            Log.e("Contador", e.getMessage(), e);
        } catch (Exception ex) {
            Log.e("Contador", ex.getMessage(), ex);
        } finally {
            conn.endTransaction();
        }

        return -1;
    }

    @Override
    public long atualizar(Produto objeto, Object... chaves) {
        try {
            conn.beginTransaction();

            ContentValues contentValues = getContentValues(objeto);
            conn.update(TABELA, contentValues, "cod_barra = ?", new String[]{String.valueOf(chaves[0])});

            conn.delete("cod_barra_fornecedor", "produto = ?", new String[]{String.valueOf(chaves[0])});

            for (int i = 0; i < objeto.getCod_barra_fornecedor().size(); i++) {
                CodBarraFornecedor codBarraFornecedor = objeto.getCod_barra_fornecedor().get(i);

                ContentValues contentCodBarraFornecedor = new ContentValues();
                contentCodBarraFornecedor.put("produto", codBarraFornecedor.getProduto().getCod_barra());
                contentCodBarraFornecedor.put("codigo", codBarraFornecedor.getCodigo());

                conn.insert("cod_barra_fornecedor", null, contentCodBarraFornecedor);
            }

            conn.setTransactionSuccessful();

            return 1;
        } catch (Exception e) {
            Log.e(ActivityBase.LOG, e.getMessage(), e);
        } finally {
            conn.endTransaction();
        }

        return -1;
    }

    private ContentValues getContentValues(Produto produto) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("cod_barra", produto.getCod_barra());
        contentValues.put("descricao", produto.getDescricao());
        contentValues.put("preco", produto.getPreco());

        if (produto.getFornecedor() != null) {
            contentValues.put("fornecedor", produto.getFornecedor().getCnpj());
        } else {
            contentValues.putNull("fornecedor");
        }

        if (produto.getMarca() != null) {
            contentValues.put("marca", produto.getMarca().getId());
        } else {
            contentValues.putNull("marca");
        }

        return contentValues;
    }

    @Override
    public long deletar(Object... chaves) {
        try {
            return conn.delete(TABELA, "cod_barra = ?", new String[]{String.valueOf(chaves[0])});
        } catch (Exception e) {
            Log.e(ActivityBase.LOG, e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Produto.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (Exception e) {
            Log.e(ActivityBase.LOG, e.getMessage(), e);
        }

        return null;
    }
}
