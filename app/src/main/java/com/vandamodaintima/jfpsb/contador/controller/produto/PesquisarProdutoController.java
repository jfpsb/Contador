package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarProdutoController {
    private PesquisarView view;
    private ProdutoCursorAdapter produtoAdapter;
    private ProdutoManager produtoManager;

    public PesquisarProdutoController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        produtoManager = new ProdutoManager(conexaoBanco);
        produtoAdapter = new ProdutoCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(produtoAdapter);
    }

    public void pesquisarPorDescricao(String termo) {
        Cursor cursor = produtoManager.listarPorDescricaoCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorCodBarra(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoManager.listarPorCodBarraCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorFornecedor(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoManager.listarPorFornecedorCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorMarca(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoManager.listarPorMarcaCursor(termo);
        mudarAdapter(cursor);
    }

    private void mudarAdapter(Cursor cursor) {
        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Produtos Não Encontrados");
        }

        produtoAdapter.changeCursor(cursor);
        produtoAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    //Utilizado apenas na tela de pesquisar produto para adicionar em contagem
    public void atualizar() {
        Boolean result = produtoManager.atualizar(produtoManager.getProduto().getCod_barra());

        if (result) {
            view.mensagemAoUsuario("Produto Foi Atualizado Com Código de Barras");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto Com Código de Barras");
        }
    }

    public void carregaProduto(String id) {
        produtoManager.load(id);
    }

    public Produto getProduto() {
        return produtoManager.getProduto();
    }

    public void addCodBarraFornecedor(String codigo) {
        produtoManager.getProduto().getCod_barra_fornecedor().add(codigo);
    }
}
