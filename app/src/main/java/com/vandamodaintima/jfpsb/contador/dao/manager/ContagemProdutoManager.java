package com.vandamodaintima.jfpsb.contador.dao.manager;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

import java.util.ArrayList;

public class ContagemProdutoManager extends Manager<ContagemProduto> {
    ContagemManager contagemManager;
    ProdutoManager produtoManager;

    public ContagemProdutoManager(ConexaoBanco conexao) {
        daoEntidade = new DAOContagemProduto(conexao.conexao());
        contagemManager = new ContagemManager(conexao);
        produtoManager = new ProdutoManager(conexao);
    }

    @Override
    public ArrayList<ContagemProduto> listar() {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();

        Cursor c = listarCursor();

        if(c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    ContagemProduto contagemProduto = new ContagemProduto();

                    contagemProduto.setId(c.getInt(c.getColumnIndexOrThrow("_id")));

                    Contagem contagem = contagemManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("contagem")));
                    contagemProduto.setContagem(contagem);

                    Produto produto = produtoManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("produto")));
                    contagemProduto.setProduto(produto);

                    contagemProduto.setQuant(c.getInt(c.getColumnIndexOrThrow("quant")));

                    contagemProdutos.add(contagemProduto);
                }
            }

            c.close();
        }

        return contagemProdutos;
    }

    @Override
    public Cursor listarCursor() {
        return daoEntidade.select(null, null, null, null, null, null);
    }

    @Override
    public ContagemProduto listarPorChave(Object... chaves) {
        ContagemProduto contagemProduto = null;
        Cursor c = listarCursorPorChave(chaves[0]);

        if(c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();

                contagemProduto = new ContagemProduto();

                contagemProduto.setId(c.getInt(c.getColumnIndexOrThrow("_id")));

                Contagem contagem = contagemManager.listarPorChave(c.getInt(c.getColumnIndexOrThrow("contagem")));
                contagemProduto.setContagem(contagem);

                Produto produto = produtoManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("produto")));
                contagemProduto.setProduto(produto);

                contagemProduto.setQuant(c.getInt(c.getColumnIndexOrThrow("quant")));
            }

            c.close();
        }

        return contagemProduto;
    }

    @Override
    public Cursor listarCursorPorChave(Object... chaves) {
        return daoEntidade.select("id = ?", new String[] { String.valueOf(chaves[0]) }, null, null, null, null);
    }

    public ArrayList<ContagemProduto> listarPorContagem(int idcontagem) {
        ArrayList<ContagemProduto> contagemProdutos = new ArrayList<>();

        Cursor c = listarPorContagemCursor(idcontagem);

        if(c != null) {
            if(c.getCount() > 0) {
                while (c.moveToNext()) {
                    ContagemProduto contagemProduto = new ContagemProduto();

                    contagemProduto.setId(c.getInt(c.getColumnIndexOrThrow("_id")));

                    Contagem contagem = contagemManager.listarPorChave(idcontagem);
                    contagemProduto.setContagem(contagem);

                    Produto produto = produtoManager.listarPorChave(c.getString(c.getColumnIndexOrThrow("produto")));
                    contagemProduto.setProduto(produto);

                    contagemProduto.setQuant(c.getInt(c.getColumnIndexOrThrow("quant")));

                    contagemProdutos.add(contagemProduto);
                }
            }

            c.close();
        }

        return contagemProdutos;
    }

    public Cursor listarPorContagemCursor(int idcontagem) {
        String sql = "SELECT id as _id, contagem, produto, descricao, SUM(quant) as quant FROM contagem_produto, produto WHERE produto = cod_barra AND contagem = ? GROUP BY produto ORDER BY descricao";
        String[] selection = new String[] { String.valueOf(idcontagem) };

        return daoEntidade.selectRaw(sql, selection);
    }
}
