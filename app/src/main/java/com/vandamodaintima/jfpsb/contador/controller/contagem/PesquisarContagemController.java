package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.contagem.PesquisarContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Calendar;

public class PesquisarContagemController {
    private PesquisarView view;
    private ContagemModel contagemModel;
    private LojaModel lojaModel;
    private SimpleCursorAdapter spinnerLojaAdapter;
    private ContagemCursorAdapter contagemCursorAdapter;

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        lojaModel = new LojaModel(conexaoBanco);
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

        Cursor cursor = contagemModel.listarPorLojaPeriodoCursor(loja, dataInicial, dataFinal);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemCursorAdapter.changeCursor(cursor);
        contagemCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public void popularSpinnerLoja() {
        Cursor cursor = lojaModel.listarCursor();

        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public LojaModel retornaLojaEscolhidaSpinner(String cnpj) {
        return lojaModel.listarPorId(cnpj);
    }

    public ContagemModel retornaContagemEscolhidaListView(String loja, String data) {
        return contagemModel.listarPorId(loja, data);
    }
}
