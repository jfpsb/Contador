package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

import java.util.ArrayList;

public class MarcaManager extends Manager<Marca> {

    public MarcaManager(ConexaoBanco conexaoBanco) {
        daoEntidade = new DAOMarca(conexaoBanco.conexao());
    }

    @Override
    public ArrayList<Marca> listar() {
        ArrayList<Marca> marcas = new ArrayList<>();

        Cursor c = listarCursor();

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Marca marca = new Marca();
                marca.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
                marca.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
                marcas.add(marca);
            }
        }

        return marcas;
    }

    @Override
    public Cursor listarCursor() {
        return daoEntidade.select(null, null, null, null, "nome", null);
    }

    @Override
    public Marca listarPorChave(Object... chaves) {
        Marca marca = null;

        Cursor c = listarCursorPorChave(chaves[0]);

        if (c.getCount() > 0) {
            c.moveToFirst();
            marca = new Marca();
            marca.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
            marca.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
        }

        return marca;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        long id = (long) chaves[0];

        return daoEntidade.select("id = ?", new String[]{String.valueOf(id)}, null, null, "nome", null);
    }

    public Cursor listarCursorPorNome(String nome) {
        return daoEntidade.select("nome LIKE ?", new String[]{"%" + nome + "%"}, null, null, "nome", null);
    }
}
