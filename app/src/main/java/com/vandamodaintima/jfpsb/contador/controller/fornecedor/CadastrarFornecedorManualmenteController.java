package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private CadastrarView view;
    private DAOFornecedor daoFornecedor;

    public CadastrarFornecedorManualmenteController(CadastrarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
    }

    public void cadastrar(FornecedorModel fornecedor) {
        if (fornecedor.getNome().isEmpty()) {
            view.mensagemAoUsuario("O Nome do FornecedorModel Não Pode Ser Vazia");
            return;
        }

        Boolean result = daoFornecedor.inserir(fornecedor);

        if (result) {
            view.mensagemAoUsuario("FornecedorModel Cadastrado Com Sucesso");
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar FornecedorModel");
        }
    }
}
