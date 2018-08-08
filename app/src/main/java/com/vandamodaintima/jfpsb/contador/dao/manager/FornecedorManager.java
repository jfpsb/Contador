package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;

import java.util.ArrayList;

public class FornecedorManager extends Manager<Fornecedor> {
    public FornecedorManager(ConexaoBanco conexao) {
        daoEntidade = new DAOFornecedor(conexao.conexao());
    }

    @Override
    public ArrayList<Fornecedor> listar() {
        ArrayList<Fornecedor> fornecedores = new ArrayList<>();

        Cursor c = listarCursor();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(c.getString(c.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(c.getString(c.getColumnIndexOrThrow("fantasia")));
                fornecedor.setEmail(c.getString(c.getColumnIndexOrThrow("email")));

                fornecedores.add(fornecedor);
            }
        }

        return fornecedores;
    }

    @Override
    public Cursor listarCursor() {
        return daoEntidade.select(null, null, null, null, "nome", null);
    }

    @Override
    public Fornecedor listarPorChave(Object... chaves) {
        Fornecedor fornecedor = null;
        Cursor c = listarCursorPorChave(chaves[0]);

        if(c.getCount() > 0) {
            c.moveToFirst();

            fornecedor = new Fornecedor();

            fornecedor.setCnpj(c.getString(c.getColumnIndexOrThrow("_id")));
            fornecedor.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
            fornecedor.setFantasia(c.getString(c.getColumnIndexOrThrow("fantasia")));
            fornecedor.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        }

        return fornecedor;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        return daoEntidade.select("cnpj = ?", new String[] { String.valueOf(chaves[0]) }, null, null, "nome", null);
    }

    public Cursor listarCursorPorNomeCnpjFantasia(String termo) {
        return daoEntidade.select("nome LIKE ? OR cnpj LIKE ? OR fantasia LIKE ?", new String[] { "%" + termo + "%", "%" + termo + "%", "%" + termo + "%" }, null, null, "nome", null);
    }
}
