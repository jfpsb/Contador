package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.ArrayList;
import java.util.Date;

public class TelaLerCodigoDeBarraController {
    private ITelaLerCodigoDeBarra view;
    private ProdutoModel produtoModel;
    private ContagemModel contagemModel;
    private ContagemProdutoModel contagemProdutoModel;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;

    public TelaLerCodigoDeBarraController(ITelaLerCodigoDeBarra view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(view.getContext(), R.layout.item_contagem_produto_dialog, new ArrayList<ProdutoModel>());
    }

    public void cadastrar(int quantidade) {
        contagemProdutoModel.setId(new Date().getTime());
        contagemProdutoModel.setProduto(produtoModel);
        contagemProdutoModel.setContagem(contagemModel);
        contagemProdutoModel.setQuant(quantidade);

        Boolean result = contagemProdutoModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void cadastrar() {
        contagemProdutoModel.setId(new Date().getTime());
        contagemProdutoModel.setProduto(produtoModel);
        contagemProdutoModel.setContagem(contagemModel);
        contagemProdutoModel.setQuant(1);

        Boolean result = contagemProdutoModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void pesquisarProduto(String cod_barra) {
        if (!cod_barra.isEmpty()) {
            ArrayList<ProdutoModel> produtos = produtoModel.listarPorCodBarra(cod_barra);

            if (produtos.size() == 0) {
                view.abrirProdutoNaoEncontradoDialog();
            } else if (produtos.size() == 1) {
                produtoModel = produtos.get(0);
                cadastrar();
            } else {
                contagemProdutoDialogArrayAdapter.clear();
                contagemProdutoDialogArrayAdapter.addAll(produtos);
                contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter);
            }
        }
    }

    public ContagemProdutoModel retornarContagemProduto(String id) {
        return contagemProdutoModel.listarPorId(id);
    }

    public void carregaContagem(String id1, String id2) {
        contagemModel.load(id1, id2);
        contagemProdutoModel.setContagem(contagemModel);
    }

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public void carregaProduto(Object o) {
        if (o instanceof ProdutoModel)
            produtoModel = (ProdutoModel) o;
    }
}
