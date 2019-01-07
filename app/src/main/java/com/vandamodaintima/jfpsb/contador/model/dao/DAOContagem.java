package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Contagem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DAOContagem implements DAO<Contagem> {
    private static final String TABELA = "contagem";

    private SQLiteDatabase sqLiteDatabase;

    private SimpleDateFormat dateParaDataSQLite = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DAOLoja daoLoja;

    public DAOContagem(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        daoLoja = new DAOLoja(sqLiteDatabase);
    }

    @Override
    public Boolean inserir(Contagem contagem) {
        try {
            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", contagem.getLoja().getCnpj());
            contentValues.put("data", Contagem.getDataSQLite(contagem.getData()));
            contentValues.put("finalizada", false);

            sqLiteDatabase.insertOrThrow(TABELA, null, contentValues);

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        }

        return false;
    }

    @Override
    public Boolean atualizar(Contagem contagem, Object... chaves) {
        String loja = String.valueOf(chaves[0]);
        String data = String.valueOf(chaves[1]);

        ContentValues contentValues = new ContentValues();

        contentValues.put("finalizada", contagem.getFinalizada());

        long result = sqLiteDatabase.update(TABELA, contentValues, "loja = ? AND data = ?", new String[]{loja, data});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        String loja = String.valueOf(chaves[0]);
        String data = String.valueOf(chaves[1]);

        long result = sqLiteDatabase.delete(TABELA, "loja = ? AND data = ?", new String[]{loja, data});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return sqLiteDatabase.query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        Contagem contagem = null;

        String loja = String.valueOf(ids[0]);
        String data = String.valueOf(ids[1]);

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "loja = ? AND data = ?", new String[]{loja, data}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            contagem = new Contagem();

            contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

            try {
                contagem.setData(dateParaDataSQLite.parse(cursor.getString(cursor.getColumnIndexOrThrow("data"))));
            } catch (ParseException e) {
                Log.e(LOG, e.getMessage(), e);
            }

            contagem.setFinalizada(cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0);
        }

        cursor.close();

        return contagem;
    }

    public Cursor listarPorLojaPeriodoCursor(String loja, Calendar dataInicial, Calendar dataFinal) {
        String sql = "SELECT contagem.ROWID as _id, loja, nome, data, finalizada FROM contagem, loja WHERE loja.cnpj = contagem.loja AND loja = ? AND data BETWEEN ? AND ? ORDER BY data";

        dataInicial.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial.set(Calendar.MINUTE, 0);
        dataInicial.set(Calendar.SECOND, 0);

        dataFinal.set(Calendar.HOUR_OF_DAY, 23);
        dataFinal.set(Calendar.MINUTE, 59);
        dataFinal.set(Calendar.SECOND, 59);

        String[] selection = new String[]{loja, dateParaDataSQLite.format(dataInicial.getTime()), dateParaDataSQLite.format(dataFinal.getTime())};

        return sqLiteDatabase.rawQuery(sql, selection);
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String loja, Calendar dataInicial, Calendar dataFinal) {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = listarPorLojaPeriodoCursor(loja, dataInicial, dataFinal);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                try {
                    contagem.setData(dateParaDataSQLite.parse(cursor.getString(cursor.getColumnIndexOrThrow("data"))));
                } catch (ParseException e) {
                    Log.e(LOG, e.getMessage(), e);
                }

                contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
                contagem.setFinalizada(cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0);

                contagens.add(contagem);
            }
        }

        cursor.close();

        return contagens;
    }
}
