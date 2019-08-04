package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.view.contagem.VisualizarProdutosContagem;

public class VisualizarProdutosContagemController {
    private DAOContagemProduto daoContagemProduto;
    private VisualizarProdutosContagem view;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;

    public VisualizarProdutosContagemController(VisualizarProdutosContagem visualizarProdutosContagem, SQLiteDatabase sqLiteDatabase, Context context) {
        daoContagemProduto = new DAOContagemProduto(sqLiteDatabase);
        this.view = visualizarProdutosContagem;
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(context, null);
        visualizarProdutosContagem.setListVIewAdaper(contagemProdutoCursorAdapter);
    }

    public void pesquisar(ContagemModel contagem) {
        Cursor cursor = daoContagemProduto.listarPorContagemGroupByProdutoCursor(contagem);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos Na ContagemModel");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }
}
