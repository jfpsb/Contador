package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarFornecedorController {
    private PesquisarView view;
    private Fornecedor fornecedorModel;
    private FornecedorCursorAdapter fornecedorCursorAdapter;

    public PesquisarFornecedorController(PesquisarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new Fornecedor(conexaoBanco);
        fornecedorCursorAdapter = new FornecedorCursorAdapter(view.getContext(), null);
        view.setListViewAdapter(fornecedorCursorAdapter);
    }

    public void pesquisa(String termo) {
        Cursor cursor = fornecedorModel.listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Fornecedores Não Encontrados");
        }

        fornecedorCursorAdapter.changeCursor(cursor);
        fornecedorCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public void carregaFornecedor(String id) {
        fornecedorModel.load(id);
    }

    public Fornecedor getFornecedor() {
        return fornecedorModel;
    }
}
