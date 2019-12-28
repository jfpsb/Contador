package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Date;

public class PesquisarProdutoController {
    protected PesquisarView view;
    private ProdutoCursorAdapter produtoAdapter;
    protected ProdutoManager produtoManager;

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

    public void carregaProduto(String id) {
        produtoManager.load(id);
    }

    public void carregaProduto(Object o) {
        if (o instanceof Produto)
            produtoManager.setProduto((Produto) o);
    }

    public Produto getProduto() {
        return produtoManager.getProduto();
    }

    public void addCodBarraFornecedor(String codigo) {
        produtoManager.getProduto().getCod_barra_fornecedor().add(codigo);
    }
}
