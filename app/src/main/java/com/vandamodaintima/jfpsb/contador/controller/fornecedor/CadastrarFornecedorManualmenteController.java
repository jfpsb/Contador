package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private FornecedorManager fornecedorManager;
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void cadastrar(Fornecedor fornecedor) {
        if (fornecedor.getCnpj().trim().isEmpty()) {
            view.mensagemAoUsuario("O CNPJ do Fornecedor Não Pode Ser Vazio");
            return;
        }
        if (fornecedor.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("O Nome do Fornecedor Não Pode Ser Vazia");
            return;
        }

        fornecedorManager.setFornecedor(fornecedor);
        Boolean result = fornecedorManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }
    }
}
