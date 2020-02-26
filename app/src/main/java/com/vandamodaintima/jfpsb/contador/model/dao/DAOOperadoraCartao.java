package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.OperadoraCartao;

import java.util.ArrayList;
import java.util.List;

public class DAOOperadoraCartao implements IDAO<OperadoraCartao> {
    private ConexaoBanco conexaoBanco;
    private final String TABELA = "operadoracartao";

    public DAOOperadoraCartao(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
    }

    @Override
    public Boolean inserir(OperadoraCartao operadoraCartao) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", operadoraCartao.getNome());
            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);

            for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                ContentValues contentBancoId = new ContentValues();
                contentBancoId.put("identificador", bancoid);
                contentBancoId.put("operadoracartao", operadoraCartao.getIdentifier());
                conexaoBanco.conexao().insertOrThrow("operadorabancoid", null, contentBancoId);
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
    public Boolean inserir(List<OperadoraCartao> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (OperadoraCartao operadoraCartao : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", operadoraCartao.getNome());
                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                    ContentValues contentBancoId = new ContentValues();
                    contentBancoId.put("identificador", bancoid);
                    contentBancoId.put("operadoracartao", operadoraCartao.getIdentifier());
                    conexaoBanco.conexao().insertWithOnConflict("operadorabancoid", null, contentBancoId, SQLiteDatabase.CONFLICT_IGNORE);
                }
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
    public Boolean inserirOuAtualizar(OperadoraCartao operadoraCartao) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", operadoraCartao.getNome());
            conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

            for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                ContentValues contentBancoId = new ContentValues();
                contentBancoId.put("identificador", bancoid);
                contentBancoId.put("operadoracartao", operadoraCartao.getIdentifier());
                conexaoBanco.conexao().insertWithOnConflict("operadorabancoid", null, contentBancoId, SQLiteDatabase.CONFLICT_REPLACE);
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
    public Boolean inserirOuAtualizar(List<OperadoraCartao> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (OperadoraCartao operadoraCartao : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", operadoraCartao.getNome());
                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                    ContentValues contentBancoId = new ContentValues();
                    contentBancoId.put("identificador", bancoid);
                    contentBancoId.put("operadoracartao", operadoraCartao.getIdentifier());
                    conexaoBanco.conexao().insertWithOnConflict("operadorabancoid", null, contentBancoId, SQLiteDatabase.CONFLICT_REPLACE);
                }
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
    public Boolean atualizar(OperadoraCartao operadoraCartao, Object... chaves) {
        try {
            String identificador = (String) chaves[0];
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", operadoraCartao.getNome());

            conexaoBanco.conexao().delete("operadorabancoid", "operadoracartao = ?", new String[]{identificador});

            for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                ContentValues contentBancoId = new ContentValues();
                contentBancoId.put("identificador", bancoid);
                contentBancoId.put("operadoracartao", operadoraCartao.getIdentifier());
                conexaoBanco.conexao().insertOrThrow("operadorabancoid", null, contentBancoId);
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "nome = ?", new String[]{identificador});
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
    public Boolean deletar(Object... chaves) {
        String identificador = (String) chaves[0];
        int result = conexaoBanco.conexao().delete(TABELA, "nome = ?", new String[]{identificador});
        return result > 0;
    }

    @Override
    public void deletar(List<OperadoraCartao> lista) {
        for(OperadoraCartao operadoraCartao : lista) {
            String identificador = operadoraCartao.getNome();
            conexaoBanco.conexao().delete(TABELA, "nome = ?", new String[]{identificador});
        }
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, OperadoraCartao.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<OperadoraCartao> listar() {
        ArrayList<OperadoraCartao> operadoras = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                OperadoraCartao operadoraCartao = new OperadoraCartao();

                operadoraCartao.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                Cursor bancoids = conexaoBanco.conexao().query("operadorabancoid", null, "operadoracartao = ?", new String[]{operadoraCartao.getNome()}, null, null, null, null);

                if(bancoids.getCount() > 0) {
                    while (bancoids.moveToNext()) {
                        String id = bancoids.getString(bancoids.getColumnIndexOrThrow("identificador"));
                        operadoraCartao.getIdentificadoresBanco().add(id);
                    }

                    bancoids.close();
                }

                operadoras.add(operadoraCartao);
            }
        }

        cursor.close();
        return operadoras;
    }

    @Override
    public OperadoraCartao listarPorId(Object... ids) {
        OperadoraCartao operadoraCartao = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, OperadoraCartao.getColunas(), "nome LIKE ?", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            operadoraCartao = new OperadoraCartao();
            operadoraCartao.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

            Cursor bancoids = conexaoBanco.conexao().query("operadorabancoid", null, "operadoracartao = ?", new String[]{operadoraCartao.getNome()}, null, null, null, null);

            if(bancoids.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = bancoids.getString(bancoids.getColumnIndexOrThrow("identificador"));
                    operadoraCartao.getIdentificadoresBanco().add(id);
                }
            }
        }

        cursor.close();
        return operadoraCartao;
    }
}
