package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.SpinnerAdapter;

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

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        lojaModel = new LojaModel(conexaoBanco);
        contagemCursorAdapter = new ContagemCursorAdapter(view.getContext(), null);
        spinnerLojaAdapter = new SimpleCursorAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, null, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
        view.setListViewAdapter(contagemCursorAdapter);
    }

    public void pesquisar(Calendar dataInicial, Calendar dataFinal) {
        Cursor cursor = contagemModel.listarPorLojaPeriodoCursor(lojaModel.getCnpj(), dataInicial, dataFinal);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemCursorAdapter.changeCursor(cursor);
        contagemCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public void carregaSpinnerLoja() {
        Cursor cursor = lojaModel.listarCursor();
        spinnerLojaAdapter.changeCursor(cursor);
        spinnerLojaAdapter.notifyDataSetChanged();
    }

    public ContagemCursorAdapter getSpinnerLojaAdapter() {
        return contagemCursorAdapter;
    }

    public void carregaContagem(String loja, String data) {
        contagemModel.load(loja, data);
    }

    public void carregaLoja(String id) {
        lojaModel.load(id);
    }
}
