package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class PesquisarContagemController {
    private PesquisarView view;
    private Contagem contagemModel;
    private Loja lojaModel;
    private ContagemArrayAdapter contagemArrayAdapter;

    public PesquisarContagemController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new Contagem(conexaoBanco);
        lojaModel = new Loja(conexaoBanco);
        contagemArrayAdapter = new ContagemArrayAdapter(view.getContext(), R.layout.item_pesquisa_contagem, new ArrayList<Contagem>());
        view.setListViewAdapter(contagemArrayAdapter);
    }

    public void pesquisar(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        ArrayList<Contagem> contagems = contagemModel.listarPorLojaPeriodo(contagemModel.getLoja().getCnpj(), dataInicial, dataFinal);

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
            contagemModel.setLoja((Loja) o);
    }

    public List<Loja> getLojas() {
        return lojaModel.listar();
    }
}
