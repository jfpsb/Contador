package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOLoja extends ADAO<Loja> {
    public DAOLoja(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "loja";
    }

    @Override
    public Boolean inserir(Loja loja) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", loja.getCnpj());
            contentValues.put("nome", loja.getNome());
            contentValues.put("telefone", loja.getTelefone());
            contentValues.put("endereco", loja.getEndereco());
            contentValues.put("inscricaoestadual", loja.getInscricaoEstadual());

            if (loja.getMatriz() != null)
                contentValues.put("matriz", loja.getMatriz().getCnpj());
            else
                contentValues.putNull("matriz");

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
    public Boolean inserir(List<Loja> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Loja loja : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cnpj", loja.getCnpj());
                contentValues.put("nome", loja.getNome());
                contentValues.put("telefone", loja.getTelefone());
                contentValues.put("endereco", loja.getEndereco());
                contentValues.put("inscricaoestadual", loja.getInscricaoEstadual());

                if (loja.getMatriz() != null)
                    contentValues.put("matriz", loja.getMatriz().getCnpj());
                else
                    contentValues.putNull("matriz");

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
    public Boolean atualizar(Loja loja) {
        try {
            String cnpj = (String) loja.getIdentifier();

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", loja.getNome());
            contentValues.put("telefone", loja.getTelefone());
            contentValues.put("endereco", loja.getEndereco());
            contentValues.put("inscricaoestadual", loja.getInscricaoEstadual());

            if (loja.getMatriz() != null)
                contentValues.put("matriz", loja.getMatriz().getCnpj());
            else
                contentValues.putNull("matriz");

            conexaoBanco.conexao().update(TABELA, contentValues, "cnpj = ?", new String[]{cnpj});
            conexaoBanco.conexao().setTransactionSuccessful();

            return true;
        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, "ERRO AO ATUALIZAR LOJA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public List<Loja> listar() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "deletado = false", null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                loja.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));
                loja.setInscricaoEstadual(cursor.getString(cursor.getColumnIndexOrThrow("inscricaoestadual")));

                if (!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                    loja.setMatriz(listarPorId(cursor.getColumnIndexOrThrow("matriz")));
                }

                lojas.add(loja);
            }
        }

        cursor.close();

        return lojas;
    }

    @Override
    public Loja listarPorId(Object... ids) {
        Loja loja = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "cnpj = ? AND deletado = false", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            loja = new Loja();

            loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
            loja.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));
            loja.setInscricaoEstadual(cursor.getString(cursor.getColumnIndexOrThrow("inscricaoestadual")));

            if (!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));
            }
        }

        cursor.close();
        return loja;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "(cnpj LIKE ? OR nome LIKE ?) AND deletado = false", new String[]{"%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<Loja> listarPorNomeCnpj(String termo) {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                loja.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));
                loja.setInscricaoEstadual(cursor.getString(cursor.getColumnIndexOrThrow("inscricaoestadual")));

                if (!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                    loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));
                }

                lojas.add(loja);
            }
        }

        cursor.close();
        return lojas;
    }

    public ArrayList<Loja> listarMatrizes() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "matriz IS NULL AND deletado = false", null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                loja.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow("endereco")));
                loja.setInscricaoEstadual(cursor.getString(cursor.getColumnIndexOrThrow("inscricaoestadual")));

                lojas.add(loja);
            }
        }

        cursor.close();

        return lojas;
    }
}
