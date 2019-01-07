package com.vandamodaintima.jfpsb.contador.controller.marca;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;
    private DAOMarca daoMarca;

    public AlterarDeletarMarcaController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoMarca = new DAOMarca(sqLiteDatabase);
    }

    public void atualizar(Marca marca) {
        if (marca.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Marca Não Pode Ser Vazio");
            return;
        }

        Boolean result = daoMarca.atualizar(marca, marca.getId());

        if (result) {
            view.mensagemAoUsuario("Marca Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Marca");
        }
    }

    public void deletar(Marca marca) {
        Boolean result = daoMarca.deletar(marca.getId());

        if (result) {
            view.mensagemAoUsuario("Marca Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Marca");
        }
    }
}
