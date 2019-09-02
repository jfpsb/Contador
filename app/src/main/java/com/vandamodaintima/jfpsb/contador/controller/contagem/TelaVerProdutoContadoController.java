package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaVerProdutoContado;

public class TelaVerProdutoContadoController {
    ITelaVerProdutoContado view;
    private ContagemModel contagemModel;
    private ContagemProdutoModel contagemProdutoModel;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;

    public TelaVerProdutoContadoController(ITelaVerProdutoContado view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        contagemProdutoModel = new ContagemProdutoModel(conexaoBanco);
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(contagemProdutoCursorAdapter);
    }

    public void deletar() {
        Boolean result = contagemProdutoModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Deletada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem de Produto");
        }
    }

    public void pesquisar() {
        Cursor cursor = contagemProdutoModel.listarPorContagemCursor(contagemModel);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }

    public void carregaContagem(String loja, String data) {
        contagemModel.load(loja, data);
    }

    public void carregaContagemProduto(long id) {
        contagemProdutoModel.load(id);
    }
}
