package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jfpsb on 08/02/2018.
 */

//TODO: Descobrir da onde vem esse memory leak (fechar cursores? Fechar conns em fragments?)
public class ConexaoBanco{
    private SQLiteDatabase conn;
    private static final String NOME_BANCO = "contagem";
    private static final int VERSAO_BANCO = 14;
    private static final String[] DELETE_DATABASE = new String[] {"DROP TABLE IF EXISTS produto", "DROP TABLE IF EXISTS contagem_produto", "DROP TABLE IF EXISTS contagem", "DROP TABLE IF EXISTS fornecedor", "DROP TABLE IF EXISTS loja"};
    private SQLiteHelper sqLiteHelper;

    private static int aberta = 0;
    private static int fechada = 0;

    private static final String[] SCRIPT_BANCO = new String[] {
            "CREATE TABLE loja (idloja INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL UNIQUE);",
            "CREATE TABLE contagem (idcontagem INTEGER PRIMARY KEY AUTOINCREMENT, loja INTEGER REFERENCES loja(idloja) NOT NULL, datainicio DATE NOT NULL, datafinal DATE NULL);",
            "CREATE TABLE fornecedor (cnpj TEXT PRIMARY KEY NOT NULL, nome TEXT NOT NULL UNIQUE);",
            "CREATE TABLE produto (cod_barra TEXT PRIMARY KEY NOT NULL, fornecedor TEXT REFERENCES fornecedor(cnpj) ON DELETE SET NULL, descricao TEXT NOT NULL, preco DOUBLE NOT NULL);",
            "CREATE TABLE contagem_produto(id INTEGER PRIMARY KEY AUTOINCREMENT, contagem INTEGER REFERENCES contagem(idcontagem) ON DELETE CASCADE, produto INTEGER REFERENCES produto(cod_barra) ON DELETE CASCADE, quant INTEGER DEFAULT 0); "
    };

    public ConexaoBanco(Context context) {
        sqLiteHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO_BANCO, SCRIPT_BANCO, DELETE_DATABASE);
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
