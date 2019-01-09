package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AdicionarContagemProdutoView;

import java.util.ArrayList;

public class AdicionarContagemProdutoController {
    AdicionarContagemProdutoView view;
    private DAOContagem daoContagem;
    private DAOContagemProduto daoContagemProduto;
    private DAOProduto daoProduto;
    private ContagemProdutoCursorAdapter contagemProdutoCursorAdapter;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;

    public AdicionarContagemProdutoController(AdicionarContagemProdutoView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        daoContagem = new DAOContagem(sqLiteDatabase);
        daoContagemProduto = new DAOContagemProduto(sqLiteDatabase);
        daoProduto = new DAOProduto(sqLiteDatabase);
        contagemProdutoCursorAdapter = new ContagemProdutoCursorAdapter(context, null);
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(context, R.layout.item_contagem_produto_dialog, new ArrayList<Produto>());
        view.setListViewAdapter(contagemProdutoCursorAdapter);
    }

    public void cadastrar(ContagemProduto contagemProduto) {
        Boolean result = daoContagemProduto.inserir(contagemProduto);

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void deletar(String id) {
        Boolean result = daoContagemProduto.deletar(id);

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Deletada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem de Produto");
        }
    }

    public void pesquisar(Contagem contagem) {
        Cursor cursor = daoContagemProduto.listarPorContagemCursor(contagem);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Não Há Produtos na Contagem");
        }

        contagemProdutoCursorAdapter.changeCursor(cursor);
        contagemProdutoCursorAdapter.notifyDataSetChanged();
    }

    public void pesquisarProduto(String cod_barra) {
        if (!cod_barra.isEmpty()) {
            ArrayList<Produto> produtos = daoProduto.listarPorCodBarra(cod_barra);

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

    public ContagemProduto retornarContagemProduto(String id) {
        return daoContagemProduto.listarPorId(id);
    }
}
