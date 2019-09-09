package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarFornecedorController {
    private PesquisarView view;
    private FornecedorManager fornecedorManager;
    private FornecedorCursorAdapter fornecedorCursorAdapter;

    public PesquisarFornecedorController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorManager = new FornecedorManager(conexaoBanco);
        fornecedorCursorAdapter = new FornecedorCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(fornecedorCursorAdapter);
    }

    public void pesquisa(String termo) {
        Cursor cursor = fornecedorManager.listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Fornecedores Não Encontrados");
        }

        fornecedorCursorAdapter.changeCursor(cursor);
        fornecedorCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public void carregaFornecedor(String id) {
        fornecedorManager.load(id);
    }

    public Fornecedor getFornecedor() {
        return fornecedorManager.getFornecedor();
    }
}
