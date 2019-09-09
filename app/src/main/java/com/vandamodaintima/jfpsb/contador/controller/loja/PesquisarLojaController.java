package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarLojaController {
    private PesquisarView view;
    private LojaManager lojaManager;
    private LojaCursorAdapter lojaCursorAdapter;

    public PesquisarLojaController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        lojaManager = new LojaManager(conexaoBanco);
        lojaCursorAdapter = new LojaCursorAdapter(context, null);
        view.setListViewAdapter(lojaCursorAdapter);
    }

    public void pesquisar(String termo) {
        Cursor cursor = lojaManager.listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Lojas Não Encontradas");
        }

        lojaCursorAdapter.changeCursor(cursor);
        lojaCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }
}
