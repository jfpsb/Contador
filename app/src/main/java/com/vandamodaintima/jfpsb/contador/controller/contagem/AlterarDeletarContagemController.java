package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemController {
    AlterarDeletarView view;
    Context context;

    public AlterarDeletarContagemController(AlterarDeletarView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void atualizar(ContagemModel contagem) {
        Boolean result = contagem.atualizar();

        if (result) {
            view.mensagemAoUsuario("Contagem Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Contagem");
        }
    }

    public void deletar(ContagemModel contagem) {
        Boolean result = contagem.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem");
        }
    }
}
