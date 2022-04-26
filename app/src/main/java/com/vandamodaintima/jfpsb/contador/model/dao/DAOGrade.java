package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAOGrade extends ADAO<Grade> {
    DAOTipoGrade daoTipoGrade;

    public DAOGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoTipoGrade = new DAOTipoGrade(conexaoBanco);
        TABELA = "grade";
    }

    @Override
    public Boolean inserir(Grade grade) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            grade.setId(UUID.randomUUID());
            contentValues.put("uuid", grade.getId().toString());
            contentValues.put("tipo", grade.getTipoGrade().getId().toString());
            contentValues.put("nome", grade.getNome());
            LocalDateTime now = LocalDateTime.now();
            contentValues.put("criadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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
                grade.setId(UUID.randomUUID());
                contentValues.put("uuid", grade.getId().toString());
                contentValues.put("tipo", grade.getTipoGrade().getId().toString());
                contentValues.put("nome", grade.getNome());
                LocalDateTime now = LocalDateTime.now();
                contentValues.put("criadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
            String id = grade.getIdentifier().toString();
            conexaoBanco.conexao().beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("tipo", grade.getTipoGrade().getId().toString());
            contentValues.put("nome", grade.getNome());
            LocalDateTime now = LocalDateTime.now();
            contentValues.put("modificadoem", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{id});
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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Grade.getColunas(), "deletado = false", null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Grade grade = new Grade(conexaoBanco);
                grade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                grade.setTipoGrade(daoTipoGrade.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))));
                grade.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                grades.add(grade);
            }
        }

        cursor.close();

        return grades;
    }

    public Cursor listarPorTipoGradeCursor(TipoGrade tipoGrade) {
        String sql = "SELECT grade.uuid as _id, grade.nome AS nome, grade.tipo AS tipo FROM grade INNER JOIN tipo_grade AS tg ON grade.tipo = tg.uuid WHERE tg.uuid = ? AND grade.deletado = false ORDER BY grade.nome";
        String[] selection = new String[]{tipoGrade.getId().toString()};
        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public List<Grade> listarPorTipoGrade(TipoGrade tipoGrade) {
        ArrayList<Grade> grades = new ArrayList<>();

        Cursor cursor = listarPorTipoGradeCursor(tipoGrade);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Grade grade = new Grade(conexaoBanco);
                grade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Grade.getColunas(), "uuid = ? AND deletado = false", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            grade = new Grade();
            cursor.moveToFirst();
            grade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
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
