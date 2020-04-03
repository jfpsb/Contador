package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarLojaController {
    private PesquisarView view;
    private Loja lojaModel;
    private LojaCursorAdapter lojaCursorAdapter;

    public PesquisarLojaController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        lojaModel = new Loja(conexaoBanco);
        lojaCursorAdapter = new LojaCursorAdapter(context, null);
        view.setListViewAdapter(lojaCursorAdapter);
    }

    public void pesquisar(String termo) {
        Cursor cursor = lojaModel.listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Lojas Não Encontradas");
        }

        lojaCursorAdapter.changeCursor(cursor);
        lojaCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }
}
