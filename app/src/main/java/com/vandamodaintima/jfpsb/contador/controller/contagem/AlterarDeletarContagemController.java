package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemController {
    AlterarDeletarView view;
    Context context;
    DAOContagem daoContagem;

    public AlterarDeletarContagemController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoContagem = new DAOContagem(sqLiteDatabase);
    }

    public void atualizar(Contagem contagem) {
        Boolean result = daoContagem.atualizar(contagem, contagem.getLoja().getCnpj(), Contagem.getDataSQLite(contagem.getData()));

        if (result) {
            view.mensagemAoUsuario("Contagem Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Contagem");
        }
    }

    public void deletar(Contagem contagem) {
        Boolean result = daoContagem.deletar(contagem.getLoja().getCnpj(), Contagem.getDataSQLite(contagem.getData()));

        if (result) {
            view.mensagemAoUsuario("Contagem Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem");
        }
    }
}
