package com.vandamodaintima.jfpsb.contador.model.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Contagem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class DAOContagem implements DAO<Contagem> {
    private static final String TABELA = "contagem";

    private SQLiteDatabase sqLiteDatabase;

    public DAOContagem(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @Override
    public Boolean inserir(Contagem contagem) {
        return null;
    }

    @Override
    public Boolean atualizar(Contagem contagem, Object... chaves) {
        return null;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        return null;
    }

    @Override
    public ArrayList<Contagem> listar() {
        return null;
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        return null;
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String loja, Date dataInicial, Date dataFinal) {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(TABELA, null, "loja = ? AND data BETWEEN ? AND ?", new String[]{loja, Contagem.getDataSQLite(dataInicial), Contagem.getDataSQLite(dataFinal)}, null, null, null, null);

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setData(Contagem.getData(cursor.getString(cursor.getColumnIndexOrThrow("data"))));
                contagem.setLoja(new DAOLoja(sqLiteDatabase).listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
                contagem.setFinalizada(cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0);

                contagens.add(contagem);
            }
        }

        cursor.close();

        return contagens;
    }
}
