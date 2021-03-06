package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOMarca extends ADAO<Marca> {
    private DAOFornecedor daoFornecedor;

    public DAOMarca(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoFornecedor = new DAOFornecedor(conexaoBanco);
        TABELA = "marca";
    }

    @Override
    public Boolean inserir(Marca marca, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", marca.getNome());
            if (marca.getFornecedor() != null) {
                contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
            }

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(marca, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<Marca> lista, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Marca marca : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", marca.getNome());
                if (marca.getFornecedor() != null) {
                    contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
                }
                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(lista, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(Marca marca, boolean sendToServer) {
        try {
            String nome = (String) marca.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", marca.getNome());
            if (marca.getFornecedor() != null) {
                contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "nome = ?", new String[]{nome});
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.atualizar(marca, sendToServer);
        } catch (Exception ex) {
            Log.e(ActivityBaseView.LOG, "ERRO AO ATUALIZAR MARCA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, Marca.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<Marca> listar() {
        ArrayList<Marca> marcas = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Marca marca = new Marca();
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                marca.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                marcas.add(marca);
            }
        }
        cursor.close();
        return marcas;
    }

    @Override
    public Marca listarPorId(Object... ids) {
        Marca marca = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Marca.getColunas(), "nome = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            marca = new Marca();
            marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            marca.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
        }

        cursor.close();
        return marca;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public Cursor listarPorNomeCursor(String nome) {
        return conexaoBanco.conexao().query(TABELA, Marca.getColunas(), "nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, null, null);
    }

    public ArrayList<Marca> listarPorNome(String nome) {
        ArrayList<Marca> marcas = new ArrayList<>();

        Cursor cursor = listarPorNomeCursor(nome);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Marca marca = new Marca();
                marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                marca.setFornecedor(daoFornecedor.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("fornecedor"))));
                marcas.add(marca);
            }
        }

        cursor.close();
        return marcas;
    }
}
