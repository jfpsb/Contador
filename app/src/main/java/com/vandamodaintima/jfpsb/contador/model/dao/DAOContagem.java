package com.vandamodaintima.jfpsb.contador.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DAOContagem extends ADAO<Contagem> {
    private DAOLoja daoLoja;
    private DAOTipoContagem daoTipoContagem;

    public DAOContagem(ConexaoBanco conexaoBanco) {
        super(conexaoBanco);
        daoLoja = new DAOLoja(conexaoBanco);
        daoTipoContagem = new DAOTipoContagem(conexaoBanco);
        TABELA = "contagem";
    }

    @Override
    public Boolean inserir(Contagem contagem) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contagem.setId(UUID.randomUUID());
            contentValues.put("uuid", contagem.getId().toString());
            contentValues.put("loja", contagem.getLoja().getCnpj());
            contentValues.put("data", contagem.getDataParaSQLite());
            contentValues.put("finalizada", contagem.getFinalizada());
            contentValues.put("tipo", contagem.getTipoContagem().getId().toString());

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
    public Boolean inserir(List<Contagem> lista) {
        try {
            conexaoBanco.conexao().beginTransaction();

            for (Contagem contagem : lista) {
                ContentValues contentValues = new ContentValues();
                contagem.setId(UUID.randomUUID());
                contentValues.put("uuid", contagem.getId().toString());
                contentValues.put("loja", contagem.getLoja().getCnpj());
                contentValues.put("data", contagem.getDataParaSQLite());
                contentValues.put("finalizada", contagem.getFinalizada());
                contentValues.put("tipo", contagem.getTipoContagem().getId().toString());

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
    public Boolean atualizar(Contagem contagem) {
        try {
            conexaoBanco.conexao().beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put("finalizada", contagem.getFinalizada());
            contentValues.put("tipo", contagem.getTipoContagem().getId().toString());

            conexaoBanco.conexao().update(TABELA, contentValues, "uuid = ?", new String[]{contagem.getId().toString()});
            conexaoBanco.conexao().setTransactionSuccessful();
            return true;

        } catch (SQLException ex) {
            Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
        } finally {
            conexaoBanco.conexao().endTransaction();
        }

        return false;
    }

    @Override
    public List<Contagem> listar() {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Contagem.getColunas(), "deletado = false", null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
                contagem.setTipoContagem(daoTipoContagem.listarPorId(cursor.getInt(cursor.getColumnIndexOrThrow("tipo"))));

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

        Cursor cursor = conexaoBanco.conexao().query(TABELA, Contagem.getColunas(), "uuid = ? AND deletado = false", new String[]{String.valueOf(ids[0])}, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            contagem = new Contagem();

            contagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
            contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
            contagem.setTipoContagem(daoTipoContagem.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))));

            String d = cursor.getString(cursor.getColumnIndexOrThrow("data"));
            contagem.setData(Contagem.convertStringToDate(d));

            boolean f = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada")) > 0;
            contagem.setFinalizada(f);
        }

        cursor.close();
        return contagem;
    }

    @Override
    public int getMaxId() {
        return 0;
    }

    public Cursor listarPorLojaPeriodoCursor(String loja, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        String sql = "SELECT contagem.uuid as _id, loja, nome, data, finalizada, tipo FROM contagem INNER JOIN loja ON loja.cnpj = contagem.loja WHERE loja = ? AND contagem.deletado = false AND data BETWEEN ? AND ? ORDER BY data";

        dataFinal = dataFinal.plusDays(1);
        dataFinal = dataFinal.minusMinutes(1);

        String[] selection = new String[]{loja, dataInicial.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), dataFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))};

        return conexaoBanco.conexao().rawQuery(sql, selection);
    }

    public ArrayList<Contagem> listarPorLojaPeriodo(String cnpj, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor cursor = listarPorLojaPeriodoCursor(cnpj, dataInicial, dataFinal);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setId(UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow("_id"))));
                contagem.setLoja(daoLoja.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("loja"))));
                contagem.setTipoContagem(daoTipoContagem.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))));

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
