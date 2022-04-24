package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAOTipoGrade extends ADAO<TipoGrade> {
    public DAOTipoGrade(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "tipo_grade";
    }

    @Override
    public Boolean inserir(TipoGrade tipoGrade) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            tipoGrade.setId(UUID.randomUUID());
            contentValues.put("uuid", tipoGrade.getId().toString());
            contentValues.put("nome", tipoGrade.getNome());

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
    public Boolean inserir(List<TipoGrade> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (TipoGrade tipoGrade : lista) {
                ContentValues contentValues = new ContentValues();
                tipoGrade.setId(UUID.randomUUID());
                contentValues.put("uuid", tipoGrade.getId().toString());
                contentValues.put("nome", tipoGrade.getNome());
                conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
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
    public Boolean atualizar(TipoGrade tipoGrade) {
        try {
            String id = tipoGrade.getIdentifier().toString();
            conexaoBanco.conexao().beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", tipoGrade.getNome());
            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{id});
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
    public List<TipoGrade> listar() {
        ArrayList<TipoGrade> tipoGrades = new ArrayList<>();
        Cursor cursor = conexaoBanco.conexao().query(TABELA, TipoGrade.getColunas(), null, null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                TipoGrade tipoGrade = new TipoGrade();
                tipoGrade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                tipoGrade.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                tipoGrades.add(tipoGrade);
            }
        }

        cursor.close();

        return tipoGrades;
    }

    @Override
    public TipoGrade listarPorId(Object... ids) {
        TipoGrade tipoGrade = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, TipoGrade.getColunas(), "uuid = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            tipoGrade = new TipoGrade();
            cursor.moveToFirst();
            tipoGrade.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
            tipoGrade.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();

        return tipoGrade;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
