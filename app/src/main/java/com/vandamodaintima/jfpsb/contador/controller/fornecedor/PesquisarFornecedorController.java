package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;

public class PesquisarFornecedorController {
    private PesquisarView view;
    DAOFornecedor daoFornecedor;
    private Context context;
    private FornecedorAdapter fornecedorAdapter;

    public PesquisarFornecedorController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
    }

    public void pesquisa(String termo) {
        ArrayList<Fornecedor> fornecedores = daoFornecedor.listarPorCnpjNomeFantasia(termo);

        if (fornecedores.size() == 0) {
            view.mensagemAoUsuario("Fornecedores Não Encontrados");
        }

        fornecedorAdapter = new FornecedorAdapter(context, R.layout.item_lista_fornecedor, fornecedores);
        view.populaLista(fornecedorAdapter);
    }
}
