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
    private DAOGrade daoGrade;

    public DAOProdutoGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "produto_grade";
        daoGrade = new DAOGrade(conexaoBanco);
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
        return null;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public List<ProdutoGrade> listarPorProduto(Produto produto) {
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "produto LIKE ?", new String[]{produto.getCodBarra()}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                pg.setProduto(produto);
                pg.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                pg.setGrades(daoGrade.listarPorProdutoGrade(pg));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }
}
