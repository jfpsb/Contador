package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarLojaController {
    private PesquisarView view;
    private LojaModel lojaModel;
    private LojaCursorAdapter lojaCursorAdapter;

    public PesquisarLojaController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        lojaModel = new LojaModel(conexaoBanco);
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

    public LojaModel retornaLojaEscolhidaListView(String cnpj) {
        return lojaModel.listarPorId(cnpj);
    }
}
