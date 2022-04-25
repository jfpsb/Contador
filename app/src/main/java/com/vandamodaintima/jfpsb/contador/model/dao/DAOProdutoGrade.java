package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        DAOSubGrade daoSubGrade = new DAOSubGrade(conexaoBanco);
        DAOProduto daoProduto = new DAOProduto(conexaoBanco);
        ProdutoGrade produtoGrade = null;
        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "uuid = ? AND deletado = false", new String[]{String.valueOf(ids[0])}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            produtoGrade = new ProdutoGrade();
            produtoGrade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
            produtoGrade.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
            produtoGrade.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
            produtoGrade.setCodBarraAlternativo(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra_alternativo")));
            produtoGrade.setPreco_custo(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_custo")));
            produtoGrade.setPreco_venda(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_venda")));
            produtoGrade.setSubGrades(daoSubGrade.listarPorProdutoGrade(produtoGrade));
        }

        cursor.close();

        return produtoGrade;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public List<ProdutoGrade> listarPorProduto(Produto produto) {
        DAOSubGrade daoSubGrade = new DAOSubGrade(conexaoBanco);
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "produto = ? AND deletado = false", new String[]{String.valueOf(produto.getId())}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                pg.setCodBarraAlternativo(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra_alternativo")));
                pg.setProduto(produto);
                pg.setPreco_custo(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_custo")));
                pg.setPreco_venda(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_venda")));
                pg.setSubGrades(daoSubGrade.listarPorProdutoGrade(pg));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }

    public List<ProdutoGrade> listarPorCodBarra(String codBarra) {
        DAOSubGrade daoSubGrade = new DAOSubGrade(conexaoBanco);
        DAOProduto daoProduto = new DAOProduto(conexaoBanco);
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "(cod_barra LIKE ? OR cod_barra_alternativo LIKE ?) AND deletado = false", new String[]{codBarra, codBarra}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra")));
                pg.setCodBarraAlternativo(cursor.getString(cursor.getColumnIndexOrThrow("cod_barra_alternativo")));
                pg.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
                pg.setPreco_custo(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_custo")));
                pg.setPreco_venda(cursor.getDouble(cursor.getColumnIndexOrThrow("preco_venda")));
                pg.setSubGrades(daoSubGrade.listarPorProdutoGrade(pg));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }
}
