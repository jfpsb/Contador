package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class PesquisarFornecedorController {
    private PesquisarView view;
    DAOFornecedor daoFornecedor;
    private Context context;
    private FornecedorCursorAdapter fornecedorCursorAdapter;

    public PesquisarFornecedorController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
        fornecedorCursorAdapter = new FornecedorCursorAdapter(context, null);
        view.setListViewAdapter(fornecedorCursorAdapter);
    }

    public void pesquisa(String termo) {
        Cursor cursor = daoFornecedor.listarPorCnpjNomeFantasiaCursor(termo);

        if (cursor.getCount() == 0) {
            view.mensagemAoUsuario("Fornecedores Não Encontrados");
        }

        fornecedorCursorAdapter.changeCursor(cursor);
        fornecedorCursorAdapter.notifyDataSetChanged();

        view.setTextoQuantidadeBusca(cursor.getCount());
    }

    public Fornecedor retornaFornecedorEscolhidoListView(String cnpj) {
        return daoFornecedor.listarPorId(cnpj);
    }
}
