package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;
import java.util.Calendar;

public class PesquisarContagemController {
    private PesquisarView view;
    private ContagemModel contagemModel;
    private LojaModel lojaModel;
    private ContagemCursorAdapter contagemCursorAdapter;

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new ContagemModel(conexaoBanco);
        lojaModel = new LojaModel(conexaoBanco);
        contagemCursorAdapter = new ContagemCursorAdapter(view.getContext(), null);
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

    public void carregaLoja(Object o) {
        if (o instanceof LojaModel)
            lojaModel = (LojaModel) o;
    }

    public ArrayList<LojaModel> getLojas() {
        return lojaModel.listar();
    }
}
