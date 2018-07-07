package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class ConexaoBanco{
    private SQLiteDatabase conn;
    private static final String NOME_BANCO = "contagem.db";
    private static final int VERSAO_BANCO = 3;
    private static final String[] DELETE_DATABASE = new String[] {"DROP TABLE IF EXISTS produto", "DROP TABLE IF EXISTS contagem_produto", "DROP TABLE IF EXISTS contagem", "DROP TABLE IF EXISTS fornecedor", "DROP TABLE IF EXISTS loja"};
    private SQLiteHelper sqLiteHelper;

    private static int aberta = 0;
    private static int fechada = 0;

    public ConexaoBanco(Context context) {
        sqLiteHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO_BANCO);
        Log.i("Contador", "ABRINDO CONN "  + (++aberta));
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

        Log.i("Contador", "Fechando CONN " + (++fechada));
    }
}
