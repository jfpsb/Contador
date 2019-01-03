package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ProdutoContagem;

import java.util.ArrayList;

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
    public ArrayList<ProdutoContagem> listar() {
        return null;
    }

    @Override
    public ArrayList<ProdutoContagem> listar(String selection, String[] args) {
        return null;
    }

    @Override
    public ProdutoContagem listarPorId(Object... ids) {
        return null;
    }
}
