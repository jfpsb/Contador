package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.ArrayList;

public class PesquisarLojaController {
    private PesquisarView view;
    private DAOLoja daoLoja;
    private LojaAdapter lojaAdapter;
    private Context context;

    public PesquisarLojaController(PesquisarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoLoja = new DAOLoja(sqLiteDatabase);
    }

    public void pesquisar(String termo) {
        ArrayList<Loja> lojas = daoLoja.selectByNomeCnpj(termo);

        if (lojas.size() == 0) {
            view.mensagemAoUsuario("Lojas Não Encontradas");
        }

        lojaAdapter = new LojaAdapter(context, R.layout.item_pesquisa_id_nome, lojas);
        view.populaLista(lojaAdapter);
    }
}
