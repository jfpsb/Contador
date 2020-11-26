package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOProdutoGrade extends ADAO<ProdutoGrade> {
    public DAOProdutoGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "produto_grade";
    }

    @Override
    public Boolean inserir(ProdutoGrade produtoGrade) {
        return null;
    }

    @Override
    public Boolean inserir(List<ProdutoGrade> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(ProdutoGrade produtoGrade) {
        return null;
    }

    @Override
    public List<ProdutoGrade> listar() {
        return null;
    }

    @Override
    public ProdutoGrade listarPorId(Object... ids) {
        DAOGrade daoGrade = new DAOGrade(conexaoBanco);
        DAOProduto daoProduto = new DAOProduto(conexaoBanco);
        ProdutoGrade produtoGrade = null;
        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            produtoGrade = new ProdutoGrade();
            produtoGrade.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            produtoGrade.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
            produtoGrade.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            produtoGrade.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
            produtoGrade.setGrades(daoGrade.listarPorProdutoGrade(produtoGrade));
        }

        cursor.close();

        return produtoGrade;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public List<ProdutoGrade> listarPorProduto(Produto produto) {
        DAOGrade daoGrade = new DAOGrade(conexaoBanco);
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "produto LIKE ?", new String[]{produto.getCodBarra()}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                pg.setProduto(produto);
                pg.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                pg.setGrades(daoGrade.listarPorProdutoGrade(pg));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }

    public List<ProdutoGrade> listarPorCodBarra(String codBarra) {
        DAOGrade daoGrade = new DAOGrade(conexaoBanco);
        DAOProduto daoProduto = new DAOProduto(conexaoBanco);
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "cod_barra LIKE ?", new String[]{codBarra}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                pg.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
                pg.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                pg.setGrades(daoGrade.listarPorProdutoGrade(pg));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }
}
