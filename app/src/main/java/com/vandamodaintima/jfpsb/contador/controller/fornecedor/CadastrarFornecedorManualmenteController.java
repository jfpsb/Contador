package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view) {
        this.view = view;
    }

    public void cadastrar(FornecedorModel fornecedor) {
        if (fornecedor.getNome().isEmpty()) {
            view.mensagemAoUsuario("O Nome do FornecedorModel Não Pode Ser Vazia");
            return;
        }

        Boolean result = fornecedor.inserir();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }
    }
}
