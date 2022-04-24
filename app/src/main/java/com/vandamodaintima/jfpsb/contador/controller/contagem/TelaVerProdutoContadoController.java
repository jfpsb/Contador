package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaVerProdutoContado;

import java.util.ArrayList;
import java.util.List;

public class TelaVerProdutoContadoController {
    ITelaVerProdutoContado view;
    private Contagem contagemModel;
    private ContagemProduto contagemProdutoModel;
    private ContagemProdutoArrayAdapter contagemProdutoArrayAdapter;

    public TelaVerProdutoContadoController(ITelaVerProdutoContado view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new Contagem(conexaoBanco);
        contagemProdutoModel = new ContagemProduto(conexaoBanco);
        contagemProdutoArrayAdapter = new ContagemProdutoArrayAdapter(view.getContext(), R.layout.item_produto_contagem_com_grade, new ArrayList<>());
        view.setListViewAdapter(contagemProdutoArrayAdapter);
    }

    public void deletar() {
        Boolean result = contagemProdutoModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Deletada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem de Produto");
        }
    }

    public void pesquisar() {
        List<ContagemProduto> lista = contagemProdutoModel.listarPorContagem(contagemProdutoModel.getContagem());

        if (lista.size() == 0) {
            view.mensagemAoUsuario("Não Há Produtos na Contagem");
        }

        contagemProdutoArrayAdapter.clear();
        contagemProdutoArrayAdapter.addAll(lista);
        contagemProdutoArrayAdapter.notifyDataSetChanged();
    }

    public void carregaContagem(String id) {
        contagemProdutoModel.setContagem(contagemModel.listarPorId(id));
    }

    public void carregaContagemProduto(long id) {
        contagemProdutoModel.load(id);
    }
}
