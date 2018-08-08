package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class ConexaoBanco {
    private SQLiteDatabase conn;
    private static final String NOME_BANCO = "contagem.db";
    private static final int VERSAO_BANCO = 3;
    private SQLiteHelper sqLiteHelper;

    public ConexaoBanco(Context context) {
        sqLiteHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO_BANCO);
        conn = sqLiteHelper.getWritableDatabase();
    }

    public SQLiteDatabase conexao() {
        return conn;
    }

    public void fechar() {
        if(conn != null)
            conn.close();

        if(sqLiteHelper != null)
            sqLiteHelper.close();
    }
}
