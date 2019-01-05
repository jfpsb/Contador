package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;
import com.vandamodaintima.jfpsb.contador.view.produto.AlterarDeletarProduto;

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
        Cursor cursor = daoProduto.listarPorCodBarraCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorFornecedor(String termo) {
        Cursor cursor = daoProduto.listarPorFornecedorCursor(termo);
        mudarAdapter(cursor);
    }

    public void pesquisarPorMarca(String termo) {
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

    public Intent abrirTelaAlterarDeletar(Cursor cursor) {
        Produto produto = daoProduto.listarPorId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

        Bundle bundle = new Bundle();
        bundle.putSerializable("produto", produto);

        Intent alterarProduto = new Intent(context, AlterarDeletarProduto.class);
        alterarProduto.putExtras(bundle);

        return alterarProduto;
    }
}
