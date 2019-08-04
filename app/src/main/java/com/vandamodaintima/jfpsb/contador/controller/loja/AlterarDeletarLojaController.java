package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarLojaController {
    AlterarDeletarView view;

    public AlterarDeletarLojaController(AlterarDeletarView view) {
        this.view = view;
    }

    public void atualizar(LojaModel loja) {
        Boolean result = loja.atualizar();

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar(LojaModel loja) {
        Boolean result = loja.deletar();

        if(result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Loja");
        }
    }
}
