package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AdicionarContagemProdutoView;

import java.util.ArrayList;

public class AdicionarContagemProdutoController {
    AdicionarContagemProdutoView view;
    private ProdutoModel produtoModel;
    private ContagemProdutoModel contagemProdutoModel;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;

    public AdicionarContagemProdutoController(AdicionarContagemProdutoView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        produtoModel = new ProdutoModel(conexaoBanco);
        contagemProdutoModel = new ContagemProdutoModel(conexaoBanco);
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(context, null);
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(context, R.layout.item_contagem_produto_dialog, new ArrayList<ProdutoModel>());
        view.setListViewAdapter(contagemProdutoCursorAdapter);
    }

    public void cadastrar(ContagemProdutoModel contagemProdutoModel) {
        Boolean result = contagemProdutoModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void deletar(ContagemProdutoModel contagemProdutoModel) {
        Boolean result = contagemProdutoModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Deletada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem de Produto");
        }
    }

    public void pesquisar(ContagemModel contagem) {
        Cursor cursor = contagemProdutoModel.listarPorContagemCursor(contagem);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }

    public void pesquisarProduto(String cod_barra) {
        if (!cod_barra.isEmpty()) {
            ArrayList<ProdutoModel> produtos = produtoModel.listarPorCodBarra(cod_barra);

            if (produtos.size() == 0) {
                view.abreProdutoNaoEncontradoDialog();
            } else if (produtos.size() == 1) {
                view.retornarProdutoEncontrado(produtos.get(0));
            } else {
                contagemProdutoDialogArrayAdapter.clear();
                contagemProdutoDialogArrayAdapter.addAll(produtos);
                contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter);
            }
        }

        view.limparCampos();
    }

    public ContagemProdutoModel retornarContagemProduto(String id) {
        return contagemProdutoModel.listarPorId(id);
    }
}
