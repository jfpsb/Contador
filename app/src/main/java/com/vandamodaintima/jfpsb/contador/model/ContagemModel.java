package com.vandamodaintima.jfpsb.contador.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContagemModel implements Serializable, IModel<ContagemModel> {
    private static final String TABELA = "contagem";
    private ConexaoBanco conexaoBanco;

    private LojaModel loja;
    private Date data;
    private Boolean finalizada;

    public ContagemModel(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        loja = new LojaModel(conexaoBanco);
    }

    private static final String[] colunas = new String[]{"ROWID as _id", "loja", "data", "finalizada"};

    public LojaModel getLoja() {
        return loja;
    }

    public void setLoja(LojaModel loja) {
        this.loja = loja;
    }

    public String getFullDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(data);
    }

    public String getShortDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(data);
    }

    public String getDataParaSQLite() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(data);
    }

    public Date convertStringToDate(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }

    public static String[] getColunas() {
        return colunas;
    }

    public static String getDataSQLite(Date data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(data);
    }

    @Override
    public Boolean inserir() {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();

            contentValues.put("loja", getLoja().getCnpj());
            contentValues.put("data", getDataSQLite(getData()));
            contentValues.put("finalizada", getFinalizada());

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

            contentValues.put("finalizada", getFinalizada());

            conexaoBanco.conexao().update(TABELA, contentValues, "loja = ? AND data = ?", new String[]{getLoja().getCnpj(), getDataParaSQLite()});
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
    public Boolean deletar() {
        long result = conexaoBanco.conexao().delete(TABELA, "loja = ? AND data = ?", new String[]{getLoja().getCnpj(), getDataParaSQLite()});

        if (result > 0)
            return true;

        return false;
    }

    @Override
    public Cursor listarCursor() {
        return conexaoBanco.conexao().query(TABELA, null, null, null, null, null, null, null);
    }

    @Override
    public ArrayList<ContagemModel> listar() {
        ArrayList<ContagemModel> contagens = new ArrayList<>();

        Cursor cursor = listarCursor();

        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ContagemModel contagem = new ContagemModel(conexaoBanco);

                contagem.setLoja(loja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

                String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                contagem.setData(convertStringToDate(d));

                boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
                contagem.setFinalizada(f);

                contagens.add(contagem);
            }
        }
        cursor.close();
        return contagens;
    }

    @Override
    public ContagemModel listarPorId(Object... ids) {
        ContagemModel contagem = null;

        Cursor cursor = conexaoBanco.conexao().query(TABELA, null, "loja = ? AND data = ?", new String[]{String.valueOf(ids[0]), String.valueOf(ids[1])}, null, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            contagem = new ContagemModel(conexaoBanco);

            contagem.setLoja(loja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));

            String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            contagem.setData(convertStringToDate(d));

            boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
            contagem.setFinalizada(f);
        }

        cursor.close();
        return contagem;
    }
}
