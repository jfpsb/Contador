package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarProdutoController {
    private PesquisarView view;
    private ProdutoCursorAdapter produtoAdapter;
    private ProdutoModel produtoModel;

    public PesquisarProdutoController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        produtoModel = new ProdutoModel(conexaoBanco);
        produtoAdapter = new ProdutoCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(produtoAdapter);
    }

    public void pesquisarPorDescricao(String termo) {
        Cursor cursor = produtoModel.listarPorDescricaoCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorCodBarra(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoModel.listarPorCodBarraCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorFornecedor(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoModel.listarPorFornecedorCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorMarca(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = produtoModel.listarPorMarcaCursor(termo);
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

    //Utilizado apenas na tela de pesquisar produtoModel para adicionar contagem
    public void atualizar() {
        Boolean result = produtoModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Foi Atualizado Com Código de Barras");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto Com Código de Barras");
        }
    }

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public String getCodBarra() {
        return produtoModel.getCod_barra();
    }

    public void addCodBarraFornecedor(String codigo) {
        produtoModel.getCod_barra_fornecedor().add(codigo);
    }
}
