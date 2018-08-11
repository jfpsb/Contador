package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

public class DAOCodBarraFornecedor extends DAO<CodBarraFornecedor> {
    public DAOCodBarraFornecedor(SQLiteDatabase conn) {
        super(conn);
        TABELA = "cod_barra_fornecedor";
    }

    @Override
    public long inserir(CodBarraFornecedor objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("produto", objeto.getProduto().getCod_barra());
            contentValues.put("codigo", objeto.getCodigo());

            long result = conn.insertOrThrow(TABELA, "", contentValues);

            return result;
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public long atualizar(CodBarraFornecedor objeto, Object... chaves) {
        return 0;
    }

    @Override
    public long deletar(Object... chaves) {
        try {
            String produto = ((Produto)chaves[0]).getCod_barra();
            String codigo = String.valueOf(chaves[1]);

            return conn.delete(TABELA, "produto = ? AND codigo = ?", new String[]{ produto, codigo });
        }
        catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, CodBarraFornecedor.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
