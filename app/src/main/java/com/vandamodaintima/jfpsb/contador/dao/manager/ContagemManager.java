package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
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

                contagem.setIdcontagem(c.getInt(c.getColumnIndexOrThrow("_id")));

                Loja loja = lojaManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("loja")));
                contagem.setLoja(loja);

                contagem.setDatainicio(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datainicio"))));
                contagem.setDatafinal(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datafim"))));

                contagens.add(contagem);
            }
        }

        return contagens;
    }

    @Override
    public Cursor listarCursor() {
        DAOContagem daoContagem = (DAOContagem)daoEntidade;

        String sql = "SELECT idcontagem as _id, loja, nome, datainicio, datafinal FROM contagem, loja WHERE loja = cnpj OR loja = null ORDER BY datainicio";

        return daoContagem.selectRaw(sql, null);
    }

    @Override
    public Contagem listarPorChave(Object... chaves) {
        Contagem contagem = null;
        Cursor c = listarCursorPorChave(chaves[0]);

        if(c != null && c.getCount() > 0) {
            c.moveToFirst();

            contagem = new Contagem();

            contagem.setIdcontagem(c.getInt(c.getColumnIndexOrThrow("_id")));

            Loja loja = lojaManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("loja")));
            contagem.setLoja(loja);

            contagem.setDatainicio(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datainicio"))));

            String datafinal = c.getString(c.getColumnIndexOrThrow("datafinal"));
            if(datafinal != null)
                contagem.setDatafinal(TrataDisplayData.getDataDoBD(datafinal));
        }

        return contagem;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        return daoEntidade.select("idcontagem = ?", new String[] { String.valueOf(chaves[0]) }, null, null, "datainicio DESC", null);
    }

    public boolean atualizarSemDataFinal(Contagem contagem, int chave) {
        DAOContagem daoContagem = (DAOContagem)daoEntidade;

        long result = daoContagem.atualizarSemDataFinal(contagem, chave);

        if(result != -1)
            return true;

        return false;
    }

    public Cursor listarPorPeriodoELoja(Date datainicio, Date datafinal, String cnpj) {
        DAOContagem daoContagem = (DAOContagem)daoEntidade;

        String sql = "SELECT idcontagem as _id, loja, nome, datainicio, datafinal FROM contagem, loja WHERE loja = cnpj AND datainicio BETWEEN ? AND ? AND loja = ? ORDER BY datainicio";

        String[] selection = new String[] { TrataDisplayData.getDataEmString(datainicio), TrataDisplayData.getDataEmString(datafinal), cnpj };

        return daoContagem.selectRaw(sql, selection);
    }
}
