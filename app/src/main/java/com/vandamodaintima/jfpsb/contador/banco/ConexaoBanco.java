package com.vandamodaintima.jfpsb.contador.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class ConexaoBanco {
    private SQLiteDatabase conn;
    private static final String NOME_BANCO = "contagem";
    private static final int VERSAO_BANCO = 1;
    private static final String DELETE_DATABASE = "DROP DATABASE IF EXISTS " + NOME_BANCO;
    private SQLiteHelper sqLiteHelper;

    private static final String[] SCRIPT_BANCO = new String[] {
            "CREATE TABLE  loja (idloja INT PRIMARY KEY NOT NULL AUTO_INCREMENT, nome TEXT NOT NULL UNIQUE);",
            "CREATE TABLE contagem` (idcontagem INT PRIMARY KEY NOT NULL AUTO_INCREMENT, loja INT REFERENCES loja(idloja), datainicio DATETIME NOT NULL, datafinal DATETIME NULL);",
            "CREATE TABLE fornecedor (cnpj text primary key NOT NULL, nome text NOT NULL);",
            "CREATE TABLE produto (cod_barra INT primary key NOT NULL, fornecedor text references fornecedor(cnpj), descricao text not NULL, preco DOUBLE not NULL);",
            "CREATE TABLE contagem_produto(contagem INT references contagem(idcontagem), produto INT references produto(cod_barra), quant INT default 0, PRIMARY KEY (contagem, produto);"
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
