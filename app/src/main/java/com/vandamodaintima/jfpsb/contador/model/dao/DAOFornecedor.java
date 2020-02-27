package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOFornecedor extends ADAO<Fornecedor> {
    public DAOFornecedor(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        TABELA = "fornecedor";
    }

    @Override
    public Boolean inserir(Fornecedor fornecedor) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", fornecedor.getCnpj());
            contentValues.put("nome", fornecedor.getNome());
            contentValues.put("fantasia", fornecedor.getFantasia());
            contentValues.put("email", fornecedor.getEmail());
            contentValues.put("telefone", fornecedor.getTelefone());

            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(fornecedor);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<Fornecedor> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Fornecedor f : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cnpj", f.getCnpj());
                contentValues.put("nome", f.getNome());
                contentValues.put("fantasia", f.getFantasia());
                contentValues.put("email", f.getEmail());
                contentValues.put("telefone", f.getTelefone());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(lista);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(Fornecedor fornecedor) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("cnpj", fornecedor.getCnpj());
            contentValues.put("nome", fornecedor.getNome());
            contentValues.put("fantasia", fornecedor.getFantasia());
            contentValues.put("email", fornecedor.getEmail());
            contentValues.put("telefone", fornecedor.getTelefone());

            conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserirOuAtualizar(fornecedor);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(List<Fornecedor> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Fornecedor f : lista) {
                ContentValues contentValues = new ContentValues();

                contentValues.put("cnpj", f.getCnpj());
                contentValues.put("nome", f.getNome());
                contentValues.put("fantasia", f.getFantasia());
                contentValues.put("email", f.getEmail());
                contentValues.put("telefone", f.getTelefone());

                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserirOuAtualizar(lista);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean atualizar(Fornecedor fornecedor, Object... chaves) {
        try {
            String cnpj = (String) chaves[0];

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", fornecedor.getNome());
            contentValues.put("fantasia", fornecedor.getFantasia());
            contentValues.put("email", fornecedor.getEmail());
            contentValues.put("telefone", fornecedor.getTelefone());

            conexaoBanco.conexao().update(TABELA, contentValues, "cnpj = ?", new String[]{cnpj});
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.atualizar(fornecedor, chaves);
        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean deletar(Object... chaves) {
        Fornecedor f = listarPorId(chaves);

        String cnpj = (String) chaves[0];

        int result = conexaoBanco.conexao().delete(TABELA, "cnpj = ?", new String[]{cnpj});

        if (result > 0) {
            escreveDatabaseLogFileDelete(f);
        }

        return result > 0;
    }

    @Override
    public void deletar(List<Fornecedor> lista) {
        for (Fornecedor fornecedor : lista) {
            String cnpj = fornecedor.getCnpj();
            int result = conexaoBanco.conexao().delete(TABELA, "cnpj = ?", new String[]{cnpj});

            if (result > 0) {
                escreveDatabaseLogFileDelete(fornecedor);
            }
        }
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, Fornecedor.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<Fornecedor> listar() {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
                fornecedores.add(fornecedor);
            }
        }
        cursor.close();
        return fornecedores;
    }

    @Override
    public Fornecedor listarPorId(Object... ids) {
        Fornecedor fornecedor = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Fornecedor.getColunas(), "cnpj = ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            fornecedor = new Fornecedor();
            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
            fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            fornecedor.setTelefone(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
        }

        cursor.close();
        return fornecedor;
    }

    public Fornecedor listarPorIdOuNome(String termo) {
        Fornecedor fornecedor = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Fornecedor.getColunas(), "cnpj = ? OR nome = ?", new String[]{termo, termo}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            fornecedor = new Fornecedor();

            fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
            fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));
        }

        cursor.close();
        return fornecedor;
    }

    public Cursor listarPorCnpjNomeFantasiaCursor(String termo) {
        return conexaoBanco.conexao().query(TABELA, Fornecedor.getColunas(), "cnpj LIKE ? OR nome LIKE ? OR fantasia LIKE ?", new String[]{"%" + termo + "%", "%" + termo + "%", "%" + termo + "%"}, null, null, "nome", null);
    }

    public ArrayList<Fornecedor> listarPorCnpjNomeFantasia(String termo) {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor cursor = listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                fornecedor.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("telefone")));

                fornecedores.add(fornecedor);
            }
        }

        cursor.close();
        return fornecedores;
    }
}
