package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarFornecedorController {
    private AlterarDeletarView view;
    private DAOFornecedor daoFornecedor;

    public AlterarDeletarFornecedorController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
    }

    public void atualizar(Fornecedor fornecedor) {
        if (fornecedor.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De Fornecedor Não Pode Ser Vazio");
            return;
        }

        Boolean result = daoFornecedor.atualizar(fornecedor, fornecedor.getCnpj());

        if (result) {
            view.mensagemAoUsuario("Fornecedor Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Fornecedor");
        }
    }

    public void deletar(Fornecedor fornecedor) {
        Boolean result = daoFornecedor.deletar(fornecedor.getCnpj());

        if (result) {
            view.mensagemAoUsuario("Fornecedor Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Fornecedor");
        }
    }
}
