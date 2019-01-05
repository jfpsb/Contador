package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;
import java.util.Date;

public class PesquisarContagemController {
    private PesquisarView view;
    private DAOContagem daoContagem;
    private ContagemCursorAdapter contagemCursorAdapter;
    private Context context;

    public PesquisarContagemController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoContagem = new DAOContagem(sqLiteDatabase);
        contagemCursorAdapter = new ContagemCursorAdapter(context, null);
        view.setListViewAdapter(contagemCursorAdapter);
    }

    public void pesquisar(String loja, Date dataInicial, Date dataFinal) {
        if (loja == "0" || loja.isEmpty()) {
            view.mensagemAoUsuario("Loja Inválida");
            return;
        }

        Cursor cursor = daoContagem.listarPorLojaPeriodoCursor(loja, dataInicial, dataFinal);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemCursorAdapter.changeCursor(cursor);
        contagemCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }
}
