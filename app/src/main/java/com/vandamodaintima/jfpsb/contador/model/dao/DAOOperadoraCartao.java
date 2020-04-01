package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.OperadoraCartao;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAOOperadoraCartao extends ADAO<OperadoraCartao> {
    public DAOOperadoraCartao(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        TABELA = "operadoracartao";
    }

    @Override
    public Boolean inserir(OperadoraCartao operadoraCartao, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", operadoraCartao.getNome());
            conexaoBanco.conexao().insertOrThrow(TABELA, null, contentValues);

            for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                ContentValues contentBancoId = new ContentValues();
                contentBancoId.put("identificador", bancoid);
                contentBancoId.put("operadoracartao", (String) operadoraCartao.getIdentifier());
                conexaoBanco.conexao().insertOrThrow("operadorabancoid", null, contentBancoId);
            }

            conexaoBanco.conexao().setTransactionSuccessful();

            return super.inserir(operadoraCartao, sendToServer);
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Boolean inserir(List<OperadoraCartao> lista, boolean sendToServer) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (OperadoraCartao operadoraCartao : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("nome", operadoraCartao.getNome());
                conexaoBanco.conexao().insertWithOnConflict(TABELA, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

                for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                    ContentValues contentBancoId = new ContentValues();
                    contentBancoId.put("identificador", bancoid);
                    contentBancoId.put("operadoracartao", (String) operadoraCartao.getIdentifier());
                    conexaoBanco.conexao().insertWithOnConflict("operadorabancoid", null, contentBancoId, SQLiteDatabase.CONFLICT_IGNORE);
                }
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
    public Boolean atualizar(OperadoraCartao operadoraCartao, boolean sendToServer, Object... chaves) {
        try {
            String identificador = (String) chaves[0];
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("nome", operadoraCartao.getNome());

            conexaoBanco.conexao().delete("operadorabancoid", "operadoracartao = ?", new String[]{identificador});

            for (String bancoid : operadoraCartao.getIdentificadoresBanco()) {
                ContentValues contentBancoId = new ContentValues();
                contentBancoId.put("identificador", bancoid);
                contentBancoId.put("operadoracartao", (String) operadoraCartao.getIdentifier());
                conexaoBanco.conexao().insertOrThrow("operadorabancoid", null, contentBancoId);
            }

            conexaoBanco.conexao().update(TABELA, contentValues, "nome = ?", new String[]{identificador});
            conexaoBanco.conexao().setTransactionSuccessful();

            return super.atualizar(operadoraCartao, sendToServer, operadoraCartao.getIdentifier());
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, OperadoraCartao.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<OperadoraCartao> listar() {
        ArrayList<OperadoraCartao> operadoras = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                OperadoraCartao operadoraCartao = new OperadoraCartao();

                operadoraCartao.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                Cursor bancoids = conexaoBanco.conexao().query("operadorabancoid", null, "operadoracartao = ?", new String[]{operadoraCartao.getNome()}, null, null, null, null);

                if (bancoids.getCount() > 0) {
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

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            operadoraCartao = new OperadoraCartao();
            operadoraCartao.setNome(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

            Cursor bancoids = conexaoBanco.conexao().query("operadorabancoid", null, "operadoracartao = ?", new String[]{operadoraCartao.getNome()}, null, null, null, null);

            if (bancoids.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = bancoids.getString(bancoids.getColumnIndexOrThrow("identificador"));
                    operadoraCartao.getIdentificadoresBanco().add(id);
                }
            }
        }

        cursor.close();
        return operadoraCartao;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
