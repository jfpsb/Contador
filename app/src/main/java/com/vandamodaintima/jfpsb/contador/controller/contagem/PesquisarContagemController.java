package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.view.contagem.PesquisarContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Calendar;

public class PesquisarContagemController {
    private PesquisarView view;
    private DAOContagem daoContagem;
    private DAOLoja daoLoja;
    private SimpleCursorAdapter spinnerLojaAdapter;
    private ContagemCursorAdapter contagemCursorAdapter;

    public PesquisarContagemController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        daoContagem = new DAOContagem(sqLiteDatabase);
        daoLoja = new DAOLoja(sqLiteDatabase);
        contagemCursorAdapter = new ContagemCursorAdapter(context, null);
        spinnerLojaAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_spinner_dropdown_item, null, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
        view.setListViewAdapter(contagemCursorAdapter);
        ((PesquisarContagem) view).setSpinnerLojaAdapter(spinnerLojaAdapter);
    }

    public void pesquisar(String loja, Calendar dataInicial, Calendar dataFinal) {
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

    public void popularSpinnerLoja() {
        Cursor cursor = daoLoja.listarCursor();

        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public Loja retornaLojaEscolhidaSpinner(String cnpj) {
        return daoLoja.listarPorId(cnpj);
    }

    public ContagemModel retornaContagemEscolhidaListView(String loja, String data) {
        return daoContagem.listarPorId(loja, data);
    }
}
