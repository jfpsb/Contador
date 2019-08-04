package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarFornecedorController {
    private PesquisarView view;
    FornecedorModel fornecedorModel;
    private Context context;
    private FornecedorCursorAdapter fornecedorCursorAdapter;

    public PesquisarFornecedorController(PesquisarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = view;
        this.context = context;
        fornecedorModel = new FornecedorModel(conexaoBanco);
        fornecedorCursorAdapter = new FornecedorCursorAdapter(context, null);
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

    public FornecedorModel retornaFornecedorEscolhidoListView(String cnpj) {
        return fornecedorModel.listarPorId(cnpj);
    }
}
