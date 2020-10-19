package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOGrade extends ADAO<Grade> {
    DAOTipoGrade daoTipoGrade;

    DAOGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoTipoGrade = new DAOTipoGrade(conexaoBanco);
        TABELA = "grade";
    }

    @Override
    public Boolean inserir(Grade grade) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("tipo", grade.getTipoGrade().getId());
            contentValues.put("nome", grade.getNome());

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
    public Boolean inserir(List<Grade> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Grade grade : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("tipo", grade.getTipoGrade().getId());
                contentValues.put("nome", grade.getNome());

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
    public Boolean atualizar(Grade grade) {
        try {
            int id = (int) grade.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("tipo", grade.getTipoGrade().getId());
            contentValues.put("nome", grade.getNome());

            conexaoBanco.conexao().update(TABELA, contentValues, "id = ?", new String[]{String.valueOf(id)});
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
    public List<Grade> listar() {
        ArrayList<Grade> grades = new ArrayList<>();

        Cursor cursor = listarCursor(Grade.getColunas());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Grade grade = new Grade();

                grade.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                grade.setTipoGrade(daoTipoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))));
                grade.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                grades.add(grade);
            }
        }

        cursor.close();

        return grades;
    }

    @Override
    public Grade listarPorId(Object... ids) {
        Grade grade = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, ProdutoGrade.getColunas(), "id = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            grade = new Grade();

            cursor.moveToFirst();

            grade.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
            grade.setTipoGrade(daoTipoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))));
            grade.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();

        return grade;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
