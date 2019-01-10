package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarProdutoController {
    private PesquisarView view;
    private ProdutoCursorAdapter produtoAdapter;
    private DAOProduto daoProduto;
    private Context context;

    public PesquisarProdutoController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoProduto = new DAOProduto(sqLiteDatabase);
        produtoAdapter = new ProdutoCursorAdapter(context, null);
        view.setListViewAdapter(produtoAdapter);
    }

    public void pesquisarPorDescricao(String termo) {
        Cursor cursor = daoProduto.listarPorDescricaoCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorCodBarra(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = daoProduto.listarPorCodBarraCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorFornecedor(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = daoProduto.listarPorFornecedorCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorMarca(String termo) {
        if (termo.isEmpty()) {
            pesquisarPorDescricao(termo);
            return;
        }

        Cursor cursor = daoProduto.listarPorMarcaCursor(termo);
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

    public Produto retornaProdutoEscolhidoListView(String cod_barra) {
        return daoProduto.listarPorId(cod_barra);
    }

    //Utilizado apenas na tela de pesquisar produto para adicionar contagem
    public void atualizar(Produto produto) {
        Boolean result = daoProduto.atualizar(produto, produto.getCod_barra());

        if(result) {
            view.mensagemAoUsuario("Produto Foi Atualizado Com Código de Barras");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto Com Código de Barras");
        }
    }
}
