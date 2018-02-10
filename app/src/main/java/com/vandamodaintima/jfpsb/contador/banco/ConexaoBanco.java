package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class ConexaoBanco{
    private SQLiteDatabase conn;
    private static final String NOME_BANCO = "contagem";
    private static final int VERSAO_BANCO = 2;
    private static final String[] DELETE_DATABASE = new String[] {"DROP TABLE IF EXISTS loja", "DROP TABLE IF EXISTS produto", "DROP TABLE IF EXISTS fornecedor", "DROP TABLE IF EXISTS contagem", "DROP TABLE IF EXISTS contagem_produto"};
    private SQLiteHelper sqLiteHelper;

    private static final String[] SCRIPT_BANCO = new String[] {
            "CREATE TABLE loja (idloja INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL UNIQUE);",
            "CREATE TABLE contagem (idcontagem INTEGER PRIMARY KEY AUTOINCREMENT, loja INT REFERENCES loja(idloja), datainicio DATE NOT NULL, datafinal DATE NULL);",
            "CREATE TABLE fornecedor (cnpj TEXT PRIMARY KEY NOT NULL, nome text NOT NULL UNIQUE);",
            "CREATE TABLE produto (cod_barra INTEGER PRIMARY KEY NOT NULL, fornecedor TEXT references fornecedor(cnpj), descricao TEXT NOT NULL, preco DOUBLE NOT NULL);",
            "CREATE TABLE contagem_produto(contagem INTEGER REFERENCES contagem(idcontagem), produto INTEGER REFERENCES produto(cod_barra), quant INTEGER DEFAULT 0, PRIMARY KEY (contagem, produto));"
    };

    public ConexaoBanco(Context context) {
        sqLiteHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO_BANCO, SCRIPT_BANCO, DELETE_DATABASE);
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
