package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.util.ArrayList;
import java.util.List;

public class DAORecebimentoCartao extends ADAO<RecebimentoCartao> {
    private DAOLoja daoLoja;
    private DAOOperadoraCartao daoOperadoraCartao;

    public DAORecebimentoCartao(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoLoja = new DAOLoja(conexaoBanco);
        daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
        TABELA = "recebimentocartao";
    }

    @Override
    public Boolean inserir(RecebimentoCartao recebimentoCartao) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("mes", recebimentoCartao.getMes());
            contentValues.put("ano", recebimentoCartao.getAno());
            contentValues.put("loja", recebimentoCartao.getLoja().getCnpj());
            contentValues.put("operadoracartao", recebimentoCartao.getOperadoraCartao().getNome());
            contentValues.put("recebido", recebimentoCartao.getRecebido());
            contentValues.put("valoroperadora", recebimentoCartao.getValorOperadora());
            contentValues.put("observacao", recebimentoCartao.getObservacao());

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
    public Boolean inserir(List<RecebimentoCartao> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (RecebimentoCartao recebimentoCartao : lista) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("mes", recebimentoCartao.getMes());
                contentValues.put("ano", recebimentoCartao.getAno());
                contentValues.put("loja", recebimentoCartao.getLoja().getCnpj());
                contentValues.put("operadoracartao", recebimentoCartao.getOperadoraCartao().getNome());
                contentValues.put("recebido", recebimentoCartao.getRecebido());
                contentValues.put("valoroperadora", recebimentoCartao.getValorOperadora());
                contentValues.put("observacao", recebimentoCartao.getObservacao());

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
    public Boolean atualizar(RecebimentoCartao recebimentoCartao) {
        try {
            Object[] chaves = (Object[]) recebimentoCartao.getIdentifier();
            String mes = (String) chaves[0];
            String ano = (String) chaves[1];
            String loja = (String) chaves[2];
            String operadoraCartao = (String) chaves[3];
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("recebido", recebimentoCartao.getRecebido());
            contentValues.put("valoroperadora", recebimentoCartao.getValorOperadora());
            contentValues.put("observacao", recebimentoCartao.getObservacao());

            conexaoBanco.conexao().update(TABELA, contentValues, "mes = ? AND ano = ? AND loja = ? AND operadoracartao = ?", new String[]{mes, ano, loja, operadoraCartao});

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
    public List<RecebimentoCartao> listar() {
        ArrayList<RecebimentoCartao> recebimentos = new ArrayList<>();

        Cursor cursor = listarCursor(RecebimentoCartao.getColunas());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                RecebimentoCartao recebimento = new RecebimentoCartao();

                recebimento.setMes(cursor.getInt(cursor.getColumnIndexOrThrow("mes")));
                recebimento.setAno(cursor.getInt(cursor.getColumnIndexOrThrow("ano")));
                recebimento.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
                recebimento.setOperadoraCartao(daoOperadoraCartao.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("operadoracartao"))));
                recebimento.setRecebido(cursor.getDouble(cursor.getColumnIndexOrThrow("recebido")));
                recebimento.setValorOperadora(cursor.getDouble(cursor.getColumnIndexOrThrow("valorOperadora")));
                recebimento.setObservacao(cursor.getString(cursor.getColumnIndexOrThrow("observacao")));

                recebimentos.add(recebimento);
            }
        }

        return recebimentos;
    }

    @Override
    public RecebimentoCartao listarPorId(Object... ids) {
        String mes = (String) ids[0];
        String ano = (String) ids[1];
        String loja = (String) ids[2];
        String operadoraCartao = (String) ids[3];

        RecebimentoCartao recebimento = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "mes = ? AND ano = ? AND loja = ? AND operadoracartao = ?", new String[]{mes, ano, loja, operadoraCartao}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            recebimento = new RecebimentoCartao();

            recebimento.setMes(cursor.getInt(cursor.getColumnIndexOrThrow("mes")));
            recebimento.setAno(cursor.getInt(cursor.getColumnIndexOrThrow("ano")));
            recebimento.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
            recebimento.setOperadoraCartao(daoOperadoraCartao.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("operadoracartao"))));
            recebimento.setRecebido(cursor.getDouble(cursor.getColumnIndexOrThrow("recebido")));
            recebimento.setValorOperadora(cursor.getDouble(cursor.getColumnIndexOrThrow("valorOperadora")));
            recebimento.setObservacao(cursor.getString(cursor.getColumnIndexOrThrow("observacao")));
        }

        cursor.close();
        return recebimento;
    }

    @Override
    public int getMaxId() {
        return 0;
    }
}
