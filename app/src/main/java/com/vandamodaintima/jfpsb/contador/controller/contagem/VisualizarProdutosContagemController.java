package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.contagem.VisualizarProdutosContagem;

public class VisualizarProdutosContagemController {
    private Contagem contagemModel;
    private ContagemProduto contagemProdutoModel;
    private VisualizarProdutosContagem view;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;

    public VisualizarProdutosContagemController(VisualizarProdutosContagem view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new Contagem(conexaoBanco);
        contagemProdutoModel = new ContagemProduto(conexaoBanco);
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(view.getApplicationContext(), null);
        view.setListViewAdaper(contagemProdutoCursorAdapter);
    }

    public void pesquisar() {
        Cursor cursor = contagemProdutoModel.listarPorContagemGroupByGradeCursor(contagemModel);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos Na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }

    public void carregaContagem(String id) {
        contagemModel.load(id);
    }
}
