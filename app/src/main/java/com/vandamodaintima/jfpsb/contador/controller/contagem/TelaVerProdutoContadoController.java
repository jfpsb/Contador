package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaVerProdutoContado;

public class TelaVerProdutoContadoController {
    ITelaVerProdutoContado view;
    private ContagemManager contagemManager;
    private ContagemProdutoManager contagemProdutoManager;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;

    public TelaVerProdutoContadoController(ITelaVerProdutoContado view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemManager = new ContagemManager(conexaoBanco);
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(contagemProdutoCursorAdapter);
    }

    public void deletar() {
        Boolean result = contagemProdutoManager.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Deletada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem de Produto");
        }
    }

    public void pesquisar() {
        Cursor cursor = contagemProdutoManager.listarPorContagemCursor(contagemProdutoManager.getContagemProduto().getContagem());

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.getContagemProduto().setContagem(contagemManager.listarPorId(loja, data));
    }

    public void carregaContagemProduto(long id) {
        contagemProdutoManager.load(id);
    }
}
