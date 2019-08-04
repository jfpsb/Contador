package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
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

    public void atualizar(ContagemModel contagem) {
        Boolean result = daoContagem.atualizar(contagem, contagem.getLoja().getCnpj(), ContagemModel.getDataSQLite(contagem.getData()));

        if (result) {
            view.mensagemAoUsuario("ContagemModel Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar ContagemModel");
        }
    }

    public void deletar(ContagemModel contagem) {
        Boolean result = daoContagem.deletar(contagem.getLoja().getCnpj(), ContagemModel.getDataSQLite(contagem.getData()));

        if (result) {
            view.mensagemAoUsuario("ContagemModel Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar ContagemModel");
        }
    }
}
