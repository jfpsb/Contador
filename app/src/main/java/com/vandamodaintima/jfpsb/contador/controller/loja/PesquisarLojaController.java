package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarLojaController {
    private PesquisarView view;
    private DAOLoja daoLoja;
    private LojaCursorAdapter lojaCursorAdapter;
    private Context context;

    public PesquisarLojaController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoLoja = new DAOLoja(sqLiteDatabase);
        lojaCursorAdapter = new LojaCursorAdapter(context, null);
        view.setListViewAdapter(lojaCursorAdapter);
    }

    public void pesquisar(String termo) {
        Cursor cursor = daoLoja.listarPorNomeCnpjCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Lojas Não Encontradas");
        }

        lojaCursorAdapter.changeCursor(cursor);
        lojaCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }
}
