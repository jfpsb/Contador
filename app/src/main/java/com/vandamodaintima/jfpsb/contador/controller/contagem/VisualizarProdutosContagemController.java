package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.contagem.VisualizarProdutosContagem;

public class VisualizarProdutosContagemController {
    private ContagemProdutoModel contagemProdutoModel;
    private VisualizarProdutosContagem view;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;

    public VisualizarProdutosContagemController(VisualizarProdutosContagem visualizarProdutosContagem, ConexaoBanco conexaoBanco, Context context) {
        contagemProdutoModel = new ContagemProdutoModel(conexaoBanco);
        this.view = visualizarProdutosContagem;
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(context, null);
        visualizarProdutosContagem.setListViewAdaper(contagemProdutoCursorAdapter);
    }

    public void pesquisar(ContagemModel contagem) {
        Cursor cursor = contagemProdutoModel.listarPorContagemGroupByProdutoCursor(contagem);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos Na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }
}
