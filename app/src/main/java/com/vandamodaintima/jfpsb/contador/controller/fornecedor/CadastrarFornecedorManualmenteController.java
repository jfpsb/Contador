package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private Fornecedor fornecedorModel;
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new Fornecedor(conexaoBanco);
    }

    public void cadastrar() {
        if (fornecedorModel.getCnpj().trim().isEmpty()) {
            view.mensagemAoUsuario("O CNPJ do Fornecedor Não Pode Ser Vazio");
            return;
        }
        if (fornecedorModel.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("O Nome do Fornecedor Não Pode Ser Vazia");
            return;
        }

        Boolean result = fornecedorModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }
    }
}
