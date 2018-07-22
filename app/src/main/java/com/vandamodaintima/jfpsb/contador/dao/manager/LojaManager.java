package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;

import java.util.ArrayList;

public class LojaManager extends Manager<Loja> {

    public LojaManager(ConexaoBanco conexao) {
        daoEntidade = new DAOLoja(conexao.conexao());
    }

    @Override
    public ArrayList<Loja> listar() {
        ArrayList<Loja> lojas = new ArrayList<>();

        Cursor c = listarCursor();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                Loja loja = new Loja();

                loja.setCnpj(c.getString(c.getColumnIndexOrThrow("_id")));
                loja.setNome(c.getString(c.getColumnIndexOrThrow("nome")));

                lojas.add(loja);
            }
        }

        return lojas;
    }

    @Override
    public Cursor listarCursor() {
        return daoEntidade.select(null, null, null, null, "nome", null);
    }

    @Override
    public Loja listarPorChave(Object... chaves) {
        Loja loja = null;
        Cursor c = listarCursorPorChave(chaves[0]);

        if(c.getCount() > 0) {
            c.moveToFirst();

            loja = new Loja();

            loja.setCnpj(c.getString(c.getColumnIndexOrThrow("_id")));
            loja.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
        }

        return loja;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        return daoEntidade.select("cnpj = ?", new String[] { String.valueOf(chaves[0]) }, null, null, "nome", null);
    }

    public Cursor listarPorNome(String nome) {
        return daoEntidade.select("nome LIKE ?", new String[] { "%" + nome + "%"}, null, null, "nome", null);
    }
}
