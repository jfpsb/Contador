package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOSubGrade extends ADAO<SubGrade> {
    private DAOProdutoGrade daoProdutoGrade;
    private DAOGrade daoGrade;

    public DAOSubGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
        daoGrade = new DAOGrade(conexaoBanco);
        TABELA = "sub_grade";
    }

    DAOSubGrade(ConexaoBanco conexaoBanco, DAOProdutoGrade daoProdutoGrade) {
        super(conexaoBanco);
        this.daoProdutoGrade = daoProdutoGrade;
        TABELA = "sub_grade";
        daoGrade = new DAOGrade(conexaoBanco);
    }

    @Override
    public Boolean inserir(SubGrade subGrade) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
            contentValues.put("grade", subGrade.getGrade().getId());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
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
    public Boolean inserir(List<SubGrade> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (SubGrade subGrade : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("produto_grade", subGrade.getProdutoGrade().getCodBarra());
                contentValues.put("grade", subGrade.getGrade().getId());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
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
    public Boolean atualizar(SubGrade subGrade) {
        return null;
    }

    @Override
    public List<SubGrade> listar() {
        return null;
    }

    @Override
    public SubGrade listarPorId(Object... ids) {
        return null;
    }

    public List<SubGrade> listarPorProdutoGrade(String produto_grade) {
        ArrayList<SubGrade> subGrades = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, SubGrade.getColunas(), "produto_grade LIKE ?", new String[]{produto_grade}, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SubGrade subGrade = new SubGrade();
                subGrade.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
                subGrade.setProdutoGrade(daoProdutoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto_grade"))));
                subGrade.setGrade(daoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("produto"))));
                subGrades.add(subGrade);
            }
        }

        cursor.close();

        return subGrades;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
