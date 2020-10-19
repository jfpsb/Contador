package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOProdutoGrade extends ADAO<ProdutoGrade> {
    private DAOProduto daoProduto;
    private DAOSubGrade daoSubGrade;

    public DAOProdutoGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoSubGrade = new DAOSubGrade(conexaoBanco);
        TABELA = "produto_grade";
        daoProduto = new DAOProduto(conexaoBanco, this);
        daoSubGrade = new DAOSubGrade(conexaoBanco, this);
    }

    public DAOProdutoGrade(ConexaoBanco conexaoBanco, DAOProduto daoProduto) {
        super(conexaoBanco);
        this.daoProduto = daoProduto;
        daoSubGrade = new DAOSubGrade(conexaoBanco, this);
        TABELA = "produto_grade";
    }

    public DAOProdutoGrade(ConexaoBanco conexaoBanco, DAOSubGrade daoSubGrade) {
        super(conexaoBanco);
        this.daoSubGrade = daoSubGrade;
        TABELA = "produto_grade";
        daoProduto = new DAOProduto(conexaoBanco, this);
    }

    @Override
    public Boolean inserir(ProdutoGrade produtoGrade) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cod_barra", produtoGrade.getCodBarra());
            contentValues.put("produto", produtoGrade.getProduto().getCodBarra());
            contentValues.put("preco", produtoGrade.getPreco());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);

            if (produtoGrade.getSubGrades().size() > 0) {
                for (SubGrade subGrade : produtoGrade.getSubGrades()) {
                    ContentValues subGradeValues = new ContentValues();
                    subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                    subGradeValues.put("grade", subGrade.getGrade().getId());
                    conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
                }
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<ProdutoGrade> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (ProdutoGrade pg : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cod_barra", pg.getCodBarra());
                contentValues.put("produto", pg.getProduto().getCodBarra());
                contentValues.put("preco", pg.getPreco());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                if (pg.getSubGrades().size() > 0) {
                    for (SubGrade subGrade : pg.getSubGrades()) {
                        ContentValues subGradeValues = new ContentValues();
                        subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                        subGradeValues.put("grade", subGrade.getGrade().getId());
                        conexaoBanco.conexao().insertWithOnConflict("sub_grade", null, subGradeValues, SQLiteDatabase.CONFLICT_IGNORE);
                    }
                }
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(ProdutoGrade produtoGrade) {
        try {
            String cod_barra = (String) produtoGrade.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("produto", produtoGrade.getProduto().getCodBarra());
            contentValues.put("preco", produtoGrade.getPreco());

            conexaoBanco.conexao().update(TABELA, contentValues, "cod_barra = ?", new String[]{cod_barra});

            conexaoBanco.conexao().delete("sub_grade", "produto_grade LIKE ?", new String[]{produtoGrade.getCodBarra()});

            if (produtoGrade.getSubGrades().size() > 0) {
                for (SubGrade subGrade : produtoGrade.getSubGrades()) {
                    ContentValues subGradeValues = new ContentValues();
                    subGradeValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                    subGradeValues.put("grade", subGrade.getGrade().getId());
                    conexaoBanco.conexao().insertOrThrow("sub_grade", null, subGradeValues);
                }
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public List<ProdutoGrade> listar() {
        ArrayList<ProdutoGrade> produtoGrades = new ArrayList<>();

        Cursor cursor = listarCursor(ProdutoGrade.getColunas());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ProdutoGrade pg = new ProdutoGrade();

                pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                pg.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
                pg.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
                pg.setSubGrades(daoSubGrade.listarPorProdutoGrade(pg.getCodBarra()));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }

    @Override
    public ProdutoGrade listarPorId(Object... ids) {
        ProdutoGrade pg = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "cod_barra = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            pg = new ProdutoGrade();

            cursor.moveToFirst();

            pg.setCodBarra(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            pg.setProduto(daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
            pg.setPreco(cursor.getDouble(cursor.getColumnIndexOrThrow("preco")));
            pg.setSubGrades(daoSubGrade.listarPorProdutoGrade(pg.getCodBarra()));
        }

        cursor.close();

        return pg;
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
                pg.setSubGrades(daoSubGrade.listarPorProdutoGrade(pg.getCodBarra()));

                produtoGrades.add(pg);
            }
        }

        cursor.close();

        return produtoGrades;
    }
}
