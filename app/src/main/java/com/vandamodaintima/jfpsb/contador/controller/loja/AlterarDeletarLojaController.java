package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarLojaController {
    AlterarDeletarView view;
    Context context;
    DAOLoja daoLoja;

    public AlterarDeletarLojaController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoLoja = new DAOLoja(sqLiteDatabase);
    }

    public void atualizar(Loja loja) {
        Boolean result = daoLoja.atualizar(loja, loja.getCnpj());

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar(Loja loja) {
        Boolean result = daoLoja.deletar(loja.getCnpj());

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Loja");
        }
    }
}
