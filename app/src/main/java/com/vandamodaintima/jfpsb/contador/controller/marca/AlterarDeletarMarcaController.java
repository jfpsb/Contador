package com.vandamodaintima.jfpsb.contador.controller.marca;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;
    private DAOMarca daoMarca;

    public AlterarDeletarMarcaController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoMarca = new DAOMarca(sqLiteDatabase);
    }

    public void atualizar(MarcaModel marca) {
        if (marca.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de MarcaModel Não Pode Ser Vazio");
            return;
        }

        Boolean result = daoMarca.atualizar(marca, marca.getId());

        if (result) {
            view.mensagemAoUsuario("MarcaModel Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar MarcaModel");
        }
    }

    public void deletar(MarcaModel marca) {
        Boolean result = daoMarca.deletar(marca.getId());

        if (result) {
            view.mensagemAoUsuario("MarcaModel Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar MarcaModel");
        }
    }
}
