package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarFornecedorController {
    private FornecedorManager fornecedorManager;
    private AlterarDeletarView view;

    public AlterarDeletarFornecedorController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void atualizar(String nome, String fantasia, String email) {
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De Fornecedor Não Pode Ser Vazio");
            return;
        }

        fornecedorManager.getFornecedor().setNome(nome);
        fornecedorManager.getFornecedor().setFantasia(fantasia);
        fornecedorManager.getFornecedor().setEmail(email);

        Boolean result = fornecedorManager.atualizar(fornecedorManager.getFornecedor().getCnpj());

        if (result) {
            view.mensagemAoUsuario("Fornecedor Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Fornecedor");
        }
    }

    public void deletar() {
        Boolean result = fornecedorManager.deletar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Fornecedor");
        }
    }

    public void carregaFornecedor(String id) {
        fornecedorManager.load(id);
    }

    public Fornecedor getFornecedor() {
        return fornecedorManager.getFornecedor();
    }
}
