package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;

    public AlterarDeletarMarcaController(AlterarDeletarView view) {
        this.view = view;
    }

    public void atualizar(MarcaModel marca) {
        if (marca.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Marca Não Pode Ser Vazio");
            return;
        }

        Boolean result = marca.atualizar();

        if (result) {
            view.mensagemAoUsuario("Marca Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Marca");
        }
    }

    public void deletar(MarcaModel marca) {
        Boolean result = marca.deletar();

        if (result) {
            view.mensagemAoUsuario("Marca Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Marca");
        }
    }
}
