package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import java.io.Serializable;
import java.util.ArrayList;

public class MarcaModel implements Serializable, IModel<MarcaModel> {
    private static final String TABELA = "marca";
    private ConexaoBanco conexaoBanco;

    private String nome;
    private static final String[] colunas = new String[]{"nome as _id"};

    public MarcaModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static String[] getColunas() {
        return colunas;
    }

    @Override
    public Boolean inserir() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", getNome());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(ArrayList<MarcaModel> lista) {
        return null;
    }

    @Override
    public Boolean atualizar() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", getNome());

            conexaoBanco.conexao().update(TABELA, contentValues, "nome = ?", new String[]{getNome()});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR MARCA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar() {
        int result = conexaoBanco.conexao().delete(TABELA, "nome = ?", new String[]{getNome()});

        if(result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public ArrayList<MarcaModel> listar() {
        ArrayList<MarcaModel> marcas = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MarcaModel marca = new MarcaModel(conexaoBanco);
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                marcas.add(marca);
            }
        }
        cursor.close();
        return marcas;
    }

    @Override
    public MarcaModel listarPorId(Object... ids) {
        MarcaModel marca = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "nome = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            marca = new MarcaModel(conexaoBanco);
            marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();
        return marca;
    }

    @Override
    public void load(Object... ids) {
        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "nome = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
        }

        cursor.close();
    }

    public Cursor listarPorNomeCursor(String nome) {
        return conexaoBanco.conexao().query(TABELA, getColunas(), "nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, null, null);
    }

    public ArrayList<MarcaModel> listarPorNome(String nome) {
        ArrayList<MarcaModel> marcas = new ArrayList<>();

        Cursor cursor = listarPorNomeCursor(nome);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                MarcaModel marca = new MarcaModel(conexaoBanco);
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                marcas.add(marca);
            }
        }

        cursor.close();
        return marcas;
    }
}
