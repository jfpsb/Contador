package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import java.io.Serializable;
import java.util.ArrayList;

public class LojaModel implements Serializable, IModel<LojaModel> {
    private static final String TABELA = "loja";
    private ConexaoBanco conexaoBanco;

    private String cnpj;
    private LojaModel matriz;
    private String nome;
    private String telefone;

    private static final String[] colunas = new String[]{"cnpj as _id", "matriz", "nome", "telefone"};

    public LojaModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LojaModel getMatriz() {
        return matriz;
    }

    public void setMatriz(LojaModel matriz) {
        this.matriz = matriz;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public static String[] getColunas() {
        return colunas;
    }

    @Override
    public Boolean inserir() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", getCnpj());
            contentValues.put("nome", getNome());
            contentValues.put("telefone", getTelefone());

            if(getMatriz() != null)
                contentValues.put("matriz", getMatriz().getCnpj());
            else
                contentValues.putNull("matriz");

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
    public Boolean atualizar() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", getNome());
            contentValues.put("telefone", getTelefone());

            if(getMatriz() != null)
                contentValues.put("matriz", getMatriz().getCnpj());
            else
                contentValues.putNull("matriz");

            conexaoBanco.conexao().update(TABELA, contentValues, "cnpj = ?", new String[]{getCnpj()});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR LOJA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar() {
        int result = conexaoBanco.conexao().delete(TABELA, "cnpj = ?", new String[]{getCnpj()});

        if(result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public ArrayList<LojaModel> listar() {
        ArrayList<LojaModel> lojas = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                LojaModel loja = new LojaModel(conexaoBanco);

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

                if(!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                    loja.setMatriz(listarPorId(cursor.getColumnIndexOrThrow("matriz")));
                }

                lojas.add(loja);
            }
        }
        cursor.close();
        return lojas;
    }

    @Override
    public LojaModel listarPorId(Object... ids) {
        LojaModel loja = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            loja = new LojaModel(conexaoBanco);

            loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

            if(!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                loja.setMatriz(listarPorId(cursor.getColumnIndexOrThrow("matriz")));
            }
        }

        cursor.close();
        return loja;
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return conexaoBanco.conexao().query(TABELA, getColunas(), "cnpj LIKE ? OR nome LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<LojaModel> listarPorNomeCnpj(String termo) {
        ArrayList<LojaModel> lojas = new ArrayList<>();

        Cursor cursor = listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                LojaModel loja = new LojaModel(conexaoBanco);

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));

                lojas.add(loja);
            }
        }

        cursor.close();
        return lojas;
    }
}
