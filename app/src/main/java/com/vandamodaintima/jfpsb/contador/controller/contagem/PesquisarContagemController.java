package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PesquisarContagemController {
    private PesquisarView view;
    private ContagemManager contagemManager;
    private LojaManager lojaManager;
    private ContagemArrayAdapter contagemArrayAdapter;

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemManager = new ContagemManager(conexaoBanco);
        lojaManager = new LojaManager(conexaoBanco);
        contagemArrayAdapter = new ContagemArrayAdapter(view.getContext(), R.layout.item_pesquisa_contagem, new ArrayList<Contagem>());
        view.setListViewAdapter(contagemArrayAdapter);
    }

    public void pesquisar(Calendar dataInicial, Calendar dataFinal) {
        ArrayList<Contagem> contagems = contagemManager.listarPorLojaPeriodo(contagemManager.getContagem().getLoja().getCnpj(), dataInicial, dataFinal);

        if (contagems.size() == 0) {
            view.mensagemAoUsuario("Contagens Não Encontradas");
        }

        contagemArrayAdapter.clear();
        contagemArrayAdapter.addAll(contagems);
        contagemArrayAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(contagems.size());
    }

    public void carregaLoja(Object o) {
        if (o instanceof Loja)
            contagemManager.getContagem().setLoja((Loja) o);
    }

    public List<Loja> getLojas() {
        return lojaManager.listar();
    }
}
