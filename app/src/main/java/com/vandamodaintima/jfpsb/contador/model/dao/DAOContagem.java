package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

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
    public ArrayList<Contagem> listar(String selection, String[] args) {
        return null;
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        return null;
    }
}
