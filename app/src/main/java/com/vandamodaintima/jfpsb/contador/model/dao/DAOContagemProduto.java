package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ProdutoContagem;

public class DAOContagemProduto implements DAO<ProdutoContagem> {
    private static final String TABELA = "contagem_produto";

    private SQLiteDatabase sqLiteDatabase;

    public DAOContagemProduto(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }


    @Override
    public Boolean inserir(ProdutoContagem contagemProduto) {
        return null;
    }

    @Override
    public Boolean atualizar(ProdutoContagem contagemProduto, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public Cursor listar() {
        return null;
    }

    @Override
    public ProdutoContagem listarPorId(Object... ids) {
        return null;
    }
}
