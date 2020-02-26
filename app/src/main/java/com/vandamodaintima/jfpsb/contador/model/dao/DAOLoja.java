package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;

import java.util.ArrayList;
import java.util.List;

public class DAOLoja implements IDAO<Loja> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "loja";

    public DAOLoja(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
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
            Log.e(LOG, e.getMessage(), e);
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
            Log.e(LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(Loja loja) {
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

            conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
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
    public Boolean inserirOuAtualizar(List<Loja> lista) {
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

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
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
    public Boolean atualizar(Loja loja, Object... chaves) {
        try {
            String cnpj = (String) chaves[0];

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
            Log.e(LOG, "ERRO AO ATUALIZAR LOJA", ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        String cnpj = (String) chaves[0];
        int result = conexaoBanco.conexao().delete(TABELA, "cnpj = ?", new String[]{cnpj});
        return result > 0;
    }

    @Override
    public void deletar(List<Loja> lista) {
        for(Loja loja : lista) {
            String cnpj = loja.getCnpj();
            conexaoBanco.conexao().delete(TABELA, "cnpj = ?", new String[]{cnpj});
        }
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, Loja.getColunas(), null, null, null, null, "nome", null);
    }

    @Override
    public List<Loja> listar() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = listarCursor();

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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            loja = new Loja();

            loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

            if (!cursor.isNull(cursor.getColumnIndexOrThrow("matriz"))) {
                loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));
            }
        }

        cursor.close();
        return loja;
    }

    public Cursor listarPorNomeCnpjCursor(String termo) {
        return conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "cnpj LIKE ? OR nome LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
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
                loja.setMatriz(listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("matriz"))));

                lojas.add(loja);
            }
        }

        cursor.close();
        return lojas;
    }

    public ArrayList<Loja> listarMatrizes() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Loja.getColunas(), "matriz IS NULL", null, null, null, "nome", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                loja.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

                lojas.add(loja);
            }
        }

        cursor.close();

        return lojas;
    }
}
