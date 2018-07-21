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

public class ContagemManager {
    private DAOContagem daoContagem;
    private DAOLoja daoLoja;

    public ContagemManager(ConexaoBanco conexao) {
        daoContagem = new DAOContagem(conexao.conexao());
        daoLoja = new DAOLoja(conexao.conexao());
    }

    public boolean inserir(Contagem contagem) {
        long result = daoContagem.inserir(contagem);

        if(result != -1) {
            return true;
        }

        return false;
    }

    public boolean atualizar(Contagem contagem, int chave) {
        long result = daoContagem.atualizar(contagem, chave);

        if(result != -1)
            return true;

        return false;
    }

    public boolean atualizarSemDataFinal(Contagem contagem, int chave) {
        long result = daoContagem.atualizarSemDataFinal(contagem, chave);

        if(result != -1)
            return true;

        return false;
    }

    public boolean deletar(int id) {
        long result = daoContagem.deletar(id);

        if(result != -1) {
            return true;
        }

        return false;
    }

    /**
     * Lista todas as contagens no banco de dados e retorna uma lista
     * @return ArrayList de Contagem
     */
    public ArrayList<Contagem> listar() {
        ArrayList<Contagem> contagens = new ArrayList<>();

        Cursor c = listarCursor();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                Contagem contagem = new Contagem();

                contagem.setIdcontagem(c.getInt(c.getColumnIndexOrThrow("_id")));

                Loja loja = daoLoja.selectLoja(c.getInt(c.getColumnIndexOrThrow("loja")));
                contagem.setLoja(loja);

                contagem.setDatainicio(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datainicio"))));
                contagem.setDatafinal(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datafim"))));

                contagens.add(contagem);
            }
        }

        return contagens;
    }

    public Contagem listarPorId(int id) {
        Contagem contagem = null;
        Cursor c = listarCursorPorId(id);

        if(c.getCount() > 0) {
            c.moveToFirst();

            contagem = new Contagem();

            contagem.setIdcontagem(c.getInt(c.getColumnIndexOrThrow("_id")));

            Loja loja = daoLoja.selectLoja(c.getInt(c.getColumnIndexOrThrow("loja")));
            contagem.setLoja(loja);

            contagem.setDatainicio(TrataDisplayData.getDataDoBD(c.getString(c.getColumnIndexOrThrow("datainicio"))));

            String datafinal = c.getString(c.getColumnIndexOrThrow("datafinal"));
            if(datafinal != null)
                contagem.setDatafinal(TrataDisplayData.getDataDoBD(datafinal));
        }

        return contagem;
    }

    /**
     * Lista todas as contagens no banco de dados e retorna um cursor
     * @return Cursor
     */
    public Cursor listarCursor() {
        return daoContagem.select(null, null, null, null, "datainicio DESC", null);
    }

    public Cursor listarCursorPorId(int id) {
        return daoContagem.select("idcontagem = ?", new String[] { String.valueOf(id) }, null, null, "datainicio DESC", null);
    }

    public Cursor listarPorPeriodoELoja(Date datainicio, Date datafinal, Loja loja) {
        String sql = "SELECT idcontagem as _id, loja, nome, datainicio, datafinal FROM contagem, loja WHERE loja = idloja AND datainicio BETWEEN ? AND ? AND loja = ? ORDER BY datainicio";

        String[] selection = new String[] { TrataDisplayData.getDataEmString(datainicio), TrataDisplayData.getDataEmString(datafinal), String.valueOf(loja.getIdloja()) };

        return daoContagem.selectRaw(sql, selection);
    }
}
