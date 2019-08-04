package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarFornecedorController {
    private AlterarDeletarView view;

    public AlterarDeletarFornecedorController(AlterarDeletarView view) {
        this.view = view;
    }

    public void atualizar(FornecedorModel fornecedor) {
        if (fornecedor.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De Fornecedor Não Pode Ser Vazio");
            return;
        }

        Boolean result = fornecedor.atualizar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Fornecedor");
        }
    }

    public void deletar(FornecedorModel fornecedor) {
        Boolean result = fornecedor.deletar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Fornecedor");
        }
    }
}
