package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Calendar;
import java.util.List;

public class PesquisarContagemController {
    private PesquisarView view;
    private ContagemManager contagemManager;
    private LojaManager lojaManager;
    private ContagemCursorAdapter contagemCursorAdapter;

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemManager = new ContagemManager(conexaoBanco);
        lojaManager = new LojaManager(conexaoBanco);
        contagemCursorAdapter = new ContagemCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(contagemCursorAdapter);
    }

    public void pesquisar(Calendar dataInicial, Calendar dataFinal) {
        Cursor cursor = contagemManager.listarPorLojaPeriodoCursor(contagemManager.getContagem().getLoja().getCnpj(), dataInicial, dataFinal);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemCursorAdapter.changeCursor(cursor);
        contagemCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public void carregaLoja(Object o) {
        if (o instanceof Loja)
            contagemManager.getContagem().setLoja((Loja) o);
    }

    public List<Loja> getLojas() {
        return lojaManager.listar();
    }
}
