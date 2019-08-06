package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private FornecedorModel fornecedorModel;
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public void cadastrar(String cnpj, String nome, String fantasia, String email) {
        if(cnpj.trim().isEmpty()) {
            view.mensagemAoUsuario("O CNPJ do Fornecedor Não Pode Ser Vazio");
            return;
        }
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("O Nome do Fornecedor Não Pode Ser Vazia");
            return;
        }

        fornecedorModel.setCnpj(cnpj);
        fornecedorModel.setNome(nome.toUpperCase());
        fornecedorModel.setFantasia(fantasia.toUpperCase());
        fornecedorModel.setEmail(email);

        Boolean result = fornecedorModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }
    }
}
