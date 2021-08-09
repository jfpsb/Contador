package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarProdutoController {
    protected PesquisarView view;
    private ProdutoCursorAdapter produtoAdapter;
    protected Produto produtoModel;
    private ProdutoGrade produtoGradeModel; //Usado para guardar grade selecionada do produto

    public PesquisarProdutoController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        produtoModel = new Produto(conexaoBanco);
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

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public void carregaProduto(Object o) {
        if (o instanceof Produto)
            produtoModel = ((Produto) o);
    }

    public ProdutoGrade getProdutoGradeModel() {
        return produtoGradeModel;
    }

    public void setProdutoGradeModel(ProdutoGrade produtoGradeModel) {
        this.produtoGradeModel = produtoGradeModel;
    }

    public Produto getProduto() {
        return produtoModel;
    }
}
