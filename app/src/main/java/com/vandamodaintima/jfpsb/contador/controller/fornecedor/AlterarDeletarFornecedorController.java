package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarFornecedorController {
    private AlterarDeletarView view;
    private DAOFornecedor daoFornecedor;

    public AlterarDeletarFornecedorController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
    }

    public void atualizar(FornecedorModel fornecedor) {
        if (fornecedor.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De FornecedorModel Não Pode Ser Vazio");
            return;
        }

        Boolean result = daoFornecedor.atualizar(fornecedor, fornecedor.getCnpj());

        if (result) {
            view.mensagemAoUsuario("FornecedorModel Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar FornecedorModel");
        }
    }

    public void deletar(FornecedorModel fornecedor) {
        Boolean result = daoFornecedor.deletar(fornecedor.getCnpj());

        if (result) {
            view.mensagemAoUsuario("FornecedorModel Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar FornecedorModel");
        }
    }
}
