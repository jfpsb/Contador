package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController {
    private FornecedorManager fornecedorManager;
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void cadastrar(String cnpj, String nome, String fantasia, String email) {
        if (cnpj.trim().isEmpty()) {
            view.mensagemAoUsuario("O CNPJ do Fornecedor Não Pode Ser Vazio");
            return;
        }
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("O Nome do Fornecedor Não Pode Ser Vazia");
            return;
        }

        fornecedorManager.getFornecedor().setCnpj(cnpj);
        fornecedorManager.getFornecedor().setNome(nome.toUpperCase());
        fornecedorManager.getFornecedor().setFantasia(fantasia.toUpperCase());
        fornecedorManager.getFornecedor().setEmail(email);

        Boolean result = fornecedorManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }
    }
}
