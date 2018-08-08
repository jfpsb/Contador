package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import java.util.ArrayList;
import java.util.Date;

public class ContagemManager extends Manager<Contagem> {
    private LojaManager lojaManager;

    public ContagemManager(ConexaoBanco conexao) {
        daoEntidade = new DAOContagem(conexao.conexao());
        lojaManager = new LojaManager(conexao);
    }

    @Override
    public ArrayList<Contagem> listar() {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor c = listarCursor();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setRowid(c.getInt(c.getColumnIndexOrThrow("_id")));

                Loja loja = lojaManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("loja")));
                contagem.setLoja(loja);

                contagem.setData(TrataDisplayData.getDataBD(c.getString(c.getColumnIndexOrThrow("data"))));
                contagem.setFinalizada(c.getInt(c.getColumnIndexOrThrow("finalizada")) > 0);

                contagens.add(contagem);
            }
        }

        return contagens;
    }

    @Override
    public Cursor listarCursor() {
        String sql = "SELECT contagem.rowid as _id, loja, nome, data, finalizada FROM contagem, loja WHERE loja = cnpj OR loja = null ORDER BY data";

        return daoEntidade.selectRaw(sql, null);
    }

    @Override
    public Contagem listarPorChave(Object... chaves) {
        Contagem contagem = null;
        Cursor c = listarCursorPorChave(chaves[0], chaves[1]);

        if(c != null && c.getCount() > 0) {
            c.moveToFirst();

            contagem = new Contagem();

            contagem.setRowid(c.getInt(c.getColumnIndexOrThrow("_id")));

            Loja loja = lojaManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("loja")));
            contagem.setLoja(loja);

            contagem.setData(TrataDisplayData.getDataBD(c.getString(c.getColumnIndexOrThrow("data"))));
            contagem.setFinalizada(c.getInt(c.getColumnIndexOrThrow("finalizada")) > 0);
        }

        return contagem;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        String cnpj = ((Loja) chaves[0]).getCnpj();
        String data = TrataDisplayData.getDataFormatoBD((Date) chaves[1]);

        return daoEntidade.select("loja = ? AND data = ?", new String[] { cnpj, data }, null, null, "data DESC", null);
    }

    public Cursor listarPorDataELoja(Date data, String cnpj) {
        String sql = "SELECT contagem.rowid as _id, loja, nome, data, finalizada FROM contagem, loja WHERE loja = cnpj AND data = ? AND loja = ? ORDER BY data";

        String[] selection = new String[] { TrataDisplayData.getDataFormatoBD(data), cnpj };

        return daoEntidade.selectRaw(sql, selection);
    }
}
