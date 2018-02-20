package com.vandamodaintima.jfpsb.contador.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOLoja {
    private SQLiteDatabase conn;
    private final String TABELA = "loja";

    public DAOLoja(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public long[] inserir(Loja loja) {
        long[] result = new long[2];

        try {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", loja.getNome());

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

    public Loja selectLoja(int id) {
        Cursor c = conn.query(true, TABELA, null, "idloja = " + id, null, null, null, null, null);

        if(c.getCount() > 0) {
            c.moveToFirst();

            Loja loja = new Loja();

            loja.setIdloja(c.getInt(0));
            loja.setNome(c.getString(1));

            return loja;
        }

        return null;
    }

    public Cursor selectLojas() {
        try {
            return conn.query(TABELA, new String[] {"idloja _id", "nome"}, null, null, null, null, "nome");
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar lojas: " + e.toString());
            return null;
        }
    }

    public Cursor selectLojas(String nome) {
        try {
            return conn.query(TABELA, new String[] {"idloja _id", "nome"}, "nome LIKE ?", new String[] { "%" + nome + "%"}, null, null, "nome");
        } catch(SQLException e) {
            Log.e("Contador", "Erro ao buscar lojas: " + e.toString());
            return null;
        }
    }

    public int deletar(int id) {
        int result = conn.delete(TABELA, "idloja = " + String.valueOf(id), null);
        Log.i("Contador", "Deletando loja com id " + id);
        return result;
        //TODO; Colocar para tratar erro de chave estrangeira
    }

    public int atualizar(Loja loja) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("idloja", loja.getIdloja());
        contentValues.put("nome", loja.getNome());

        return conn.update(TABELA, contentValues, "idloja = ?", new String[] {String.valueOf(loja.getIdloja())});
    }
}
