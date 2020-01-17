package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DAOContagem implements IDAO<Contagem> {
    private static final String TABELA = "contagem";
    private ConexaoBanco conexaoBanco;
    private DAOLoja daoLoja;

    public DAOContagem(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoLoja = new DAOLoja(conexaoBanco);
    }

    @Override
    public Boolean inserir(Contagem contagem) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", contagem.getLoja().getCnpj());
            contentValues.put("data", contagem.getDataSQLite(contagem.getData()));
            contentValues.put("finalizada", contagem.getFinalizada());
            contentValues.put("tipo", contagem.getTipoContagem().getId());

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
    public Boolean inserir(List<Contagem> lista) {
        return null;
    }

    @Override
    public Boolean atualizar(Contagem contagem, Object... chaves) {
        try {
            String cnpj = (String) chaves[0];
            String data = (String) chaves[1];

            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("finalizada", contagem.getFinalizada());

            conexaoBanco.conexao().update(TABELA, contentValues, "loja = ? AND data = ?", new String[]{cnpj, data});
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
        String cnpj = (String) chaves[0];
        String data = (String) chaves[1];
        long result = conexaoBanco.conexao().delete(TABELA, "loja = ? AND data = ?", new String[]{cnpj, data});
        return result > 0;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, Contagem.getColunas(), null, null, null, null, null, null);
    }

    @Override
    public List<Contagem> listar() {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = listarCursor();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

                String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                contagem.setData(Contagem.convertStringToDate(d));

                boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
                contagem.setFinalizada(f);

                contagens.add(contagem);
            }
        }
        cursor.close();
        return contagens;
    }

    @Override
    public Contagem listarPorId(Object... ids) {
        Contagem contagem = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Contagem.getColunas(), "loja = ? AND data = ?", new String[]{String.valueOf(ids[0]), String.valueOf(ids[1])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            contagem = new Contagem();

            contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

            String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            contagem.setData(Contagem.convertStringToDate(d));

            boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
            contagem.setFinalizada(f);
        }

        cursor.close();
        return contagem;
    }

    public Cursor listarPorLojaPeriodoCursor(String loja, Calendar dataInicial, Calendar dataFinal) {
        String sql = "SELECT contagem.ROWID as _id, loja, nome, data, finalizada, tipo FROM contagem, loja WHERE loja.cnpj = contagem.loja AND loja = ? AND data BETWEEN ? AND ? ORDER BY data";

        dataInicial.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial.set(Calendar.MINUTE, 0);
        dataInicial.set(Calendar.SECOND, 0);

        dataFinal.set(Calendar.HOUR_OF_DAY, 23);
        dataFinal.set(Calendar.MINUTE, 59);
        dataFinal.set(Calendar.SECOND, 59);

        String[] selection = new String[]{loja, Contagem.getDataSQLite(dataInicial.getTime()), Contagem.getDataSQLite(dataFinal.getTime())};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String cnpj, Calendar dataInicial, Calendar dataFinal) {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = listarPorLojaPeriodoCursor(cnpj, dataInicial, dataFinal);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

                String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                contagem.setData(Contagem.convertStringToDate(d));

                boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
                contagem.setFinalizada(f);

                contagens.add(contagem);
            }
        }

        cursor.close();

        return contagens;
    }
}