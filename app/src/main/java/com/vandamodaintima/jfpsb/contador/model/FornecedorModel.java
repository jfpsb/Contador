package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.FontsContract;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jfpsb on 08/02/2018.
 */

public class FornecedorModel implements Serializable, IModel<FornecedorModel> {
    private static final String TABELA = "fornecedor";
    private ConexaoBanco conexaoBanco;

    private String cnpj;
    private String nome;
    private String fantasia;
    private String email;

    public FornecedorModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    private static final String[] colunas = new String[]{"cnpj as _id", "nome", "fantasia", "email"};

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
            contentValues.put("fantasia", getFantasia());
            contentValues.put("email", getEmail());

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
            contentValues.put("fantasia", getFantasia());
            contentValues.put("email", getEmail());

            conexaoBanco.conexao().update(TABELA, contentValues, "cnpj = ?", new String[]{getCnpj()});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(LOG, "ERRO AO ATUALIZAR FORNECEDOR", ex);
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
    public ArrayList<FornecedorModel> listar() {
        ArrayList<FornecedorModel> fornecedores = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                FornecedorModel fornecedor = new FornecedorModel(conexaoBanco);

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                fornecedores.add(fornecedor);
            }
        }
        cursor.close();
        return fornecedores;
    }

    @Override
    public FornecedorModel listarPorId(Object... ids) {
        FornecedorModel fornecedor = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            fornecedor = new FornecedorModel(conexaoBanco);
            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
            fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }

        cursor.close();
        return fornecedor;
    }

    public FornecedorModel listarPorIdOuNome(String termo) {
        FornecedorModel fornecedor = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "cnpj = ? OR nome = ?", new String[]{termo, termo}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            fornecedor = new FornecedorModel(conexaoBanco);

            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
            fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }

        cursor.close();
        return fornecedor;
    }

    public Cursor listarPorCnpjNomeFantasiaCursor(String termo) {
        return conexaoBanco.conexao().query(TABELA, getColunas(), "cnpj LIKE ? OR nome LIKE ? OR fantasia LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<FornecedorModel> listarPorCnpjNomeFantasia(String termo) {
        ArrayList<FornecedorModel> fornecedores = new ArrayList<>();

        Cursor cursor = listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                FornecedorModel fornecedor = new FornecedorModel(conexaoBanco);

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));

                fornecedores.add(fornecedor);
            }
        }

        cursor.close();
        return fornecedores;
    }
}
