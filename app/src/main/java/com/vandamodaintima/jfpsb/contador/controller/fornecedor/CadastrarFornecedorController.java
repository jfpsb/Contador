package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.CadastrarFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorController {
    protected CadastrarFornecedor view;
    private FornecedorManager fornecedorManager;
    private ConexaoBanco conexaoBanco;

    public CadastrarFornecedorController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarFornecedor) view;
        this.conexaoBanco = conexaoBanco;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void cadastrar(Fornecedor fornecedor) {
        if (fornecedor.getCnpj().isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Ser Vazio");
            return;
        }

        if (fornecedor.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
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

    public void pesquisarNaReceita(String cnpj) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Ficar Vazio");
            return;
        }
        new RetornarFornecedor<>(view).execute("https://www.receitaws.com.br/v1/cnpj/", cnpj);
    }

    public void checaCnpj(String cnpj) {
        if (!cnpj.isEmpty()) {
            Fornecedor fornecedor = fornecedorManager.listarPorId(cnpj);

            if (fornecedor != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }

    public Fornecedor getFornecedor() {
        return fornecedorManager.getFornecedor();
    }
}
