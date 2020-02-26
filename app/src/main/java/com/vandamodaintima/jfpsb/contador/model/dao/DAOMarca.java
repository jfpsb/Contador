package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;

import java.util.ArrayList;
import java.util.List;

public class DAOMarca implements IDAO<Marca> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "marca";
    private DAOFornecedor daoFornecedor;

    public DAOMarca(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoFornecedor = new DAOFornecedor(conexaoBanco);
    }

    @Override
    public Boolean inserir(Marca marca) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", marca.getNome());
            if (marca.getFornecedor() != null) {
                contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
            }

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
    public Boolean inserir(List<Marca> lista) {
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

            return true;
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserirOuAtualizar(Marca marca) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", marca.getNome());
            if (marca.getFornecedor() != null) {
                contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
            }

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
    public Boolean inserirOuAtualizar(List<Marca> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Marca marca : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", marca.getNome());
                if (marca.getFornecedor() != null) {
                    contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
                }
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
    public Boolean atualizar(Marca marca, Object... chaves) {
        try {
            String nome = (String) chaves[0];

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", marca.getNome());
            if (marca.getFornecedor() != null) {
                contentValues.put("fornecedor", marca.getFornecedor().getCnpj());
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "nome = ?", new String[]{nome});
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
    public Boolean deletar(Object... chaves) {
        String nome = (String) chaves[0];
        int result = conexaoBanco.conexao().delete(TABELA, "nome = ?", new String[]{nome});
        return result > 0;
    }

    @Override
    public void deletar(List<Marca> lista) {
        for(Marca marca : lista) {
            String nome = marca.getNome();
            conexaoBanco.conexao().delete(TABELA, "nome = ?", new String[]{nome});
        }
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
