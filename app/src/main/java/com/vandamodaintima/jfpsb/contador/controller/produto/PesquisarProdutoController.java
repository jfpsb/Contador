package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;

public class PesquisarProdutoController {
    private PesquisarView view;
    private ProdutoAdapter produtoAdapter;
    private DAOProduto daoProduto;
    private Context context;

    public PesquisarProdutoController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoProduto = new DAOProduto(sqLiteDatabase);
    }

    public void pesquisarPorDescricao(String termo) {
        ArrayList<Produto> produtos = daoProduto.listarPorDescricao(termo);

        if (produtos.size() == 0) {
            view.mensagemAoUsuario("Produtos Não Encontrados");
        }

        produtoAdapter = new ProdutoAdapter(context, R.layout.item_pesquisa_produto, produtos);
        view.populaLista(produtoAdapter);
    }

    public void pesquisarPorCodBarra(String termo) {
        ArrayList<Produto> produtos = daoProduto.listarPorCodBarra(termo);

        if (produtos.size() == 0) {
            view.mensagemAoUsuario("Produtos Não Encontrados");
        }

        produtoAdapter = new ProdutoAdapter(context, R.layout.item_pesquisa_produto, produtos);
        view.populaLista(produtoAdapter);
    }

    public void pesquisarPorFornecedor(String termo) {
        ArrayList<Produto> produtos = daoProduto.listarPorFornecedor(termo);

        if (produtos.size() == 0) {
            view.mensagemAoUsuario("Produtos Não Encontrados");
        }

        produtoAdapter = new ProdutoAdapter(context, R.layout.item_pesquisa_produto, produtos);
        view.populaLista(produtoAdapter);
    }

    public void pesquisarPorMarca(String termo) {
        ArrayList<Produto> produtos = daoProduto.listarPorMarca(termo);

        if (produtos.size() == 0) {
            view.mensagemAoUsuario("Produtos Não Encontrados");
        }

        produtoAdapter = new ProdutoAdapter(context, R.layout.item_pesquisa_produto, produtos);
        view.populaLista(produtoAdapter);
    }
}
