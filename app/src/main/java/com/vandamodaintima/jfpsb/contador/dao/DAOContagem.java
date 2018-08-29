package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOContagem extends DAO<Contagem> {
    public DAOContagem(SQLiteDatabase conn) {
        super(conn);
        TABELA = "contagem";
    }

    @Override
    public long inserir(Contagem objeto) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", objeto.getLoja().getCnpj());
            contentValues.put("data", TrataDisplayData.getDataFormatoBD(objeto.getData()));

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
            String cnpj = ((Loja) chaves[0]).getCnpj();
            String data = TrataDisplayData.getDataFormatoBD((Date) chaves[1]);

            ContentValues contentValues = new ContentValues();

            contentValues.put("finalizada", objeto.getFinalizada());

            return conn.update(TABELA, contentValues, "loja = ? AND data = ?", new String[]{ cnpj, data});
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return -1;
    }

    @Override
    public long deletar(Object... chaves) {
        try {
            conn.beginTransaction();

            String cnpj = ((Loja) chaves[0]).getCnpj();
            String data = TrataDisplayData.getDataFormatoBD((Date) chaves[1]);

            conn.delete("contagem_produto", "contagem_loja = ? AND contagem_data = ?", new String[]{ cnpj, data });

            conn.delete(TABELA, "loja = ? AND data = ?", new String[]{ cnpj, data });

            conn.setTransactionSuccessful();

            return 1;
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        } finally {
            conn.endTransaction();
        }

        return -1;
    }

    @Override
    public Cursor select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        try {
            return conn.query(TABELA, Contagem.getColunas(), selection, selectionArgs, groupBy, having, orderBy, limit);
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }

        return null;
    }
}
