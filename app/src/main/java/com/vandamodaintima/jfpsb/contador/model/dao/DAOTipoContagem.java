package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DAOTipoContagem extends ADAO<TipoContagem> {
    public DAOTipoContagem(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "tipo_contagem";
    }

    @Override
    public Boolean inserir(TipoContagem tipoContagem) {
        try {
            conexaoBanco.conexao().beginTransaction();
            ContentValues contentValues = new ContentValues();
            tipoContagem.setId(UUID.randomUUID());
            contentValues.put("uuid", tipoContagem.getId().toString());
            contentValues.put("nome", tipoContagem.getNome());
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
    public Boolean inserir(List<TipoContagem> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (TipoContagem tipoContagem : lista) {
                ContentValues contentValues = new ContentValues();
                tipoContagem.setId(UUID.randomUUID());
                contentValues.put("uuid", tipoContagem.getId().toString());
                contentValues.put("nome", tipoContagem.getNome());
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
    public Boolean atualizar(TipoContagem tipoContagem) {
        try {
            String id = tipoContagem.getIdentifier().toString();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", tipoContagem.getNome());

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
    public List<TipoContagem> listar() {
        ArrayList<TipoContagem> tipoContagems = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, TipoContagem.getColunas(), null, null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                TipoContagem tipoContagem = new TipoContagem();
                tipoContagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(("_id")))));
                tipoContagem.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                tipoContagems.add(tipoContagem);
            }
        }
        cursor.close();
        return tipoContagems;
    }

    @Override
    public TipoContagem listarPorId(Object... ids) {
        TipoContagem tipoContagem = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, TipoContagem.getColunas(), "uuid = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tipoContagem = new TipoContagem();
            tipoContagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(("_id")))));
            tipoContagem.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();

        return tipoContagem;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public Cursor listarPorNomeCursor(String nome) {
        return conexaoBanco.conexao().query(TABELA, TipoContagem.getColunas(), "nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, null, null);
    }

    public ArrayList<TipoContagem> listarPorNome(String nome) {
        ArrayList<TipoContagem> tipoContagems = new ArrayList<>();

        Cursor cursor = listarPorNomeCursor(nome);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                TipoContagem tipoContagem = new TipoContagem();
                tipoContagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(("_id")))));
                tipoContagem.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                tipoContagems.add(tipoContagem);
            }
        }

        cursor.close();

        return tipoContagems;
    }
}
