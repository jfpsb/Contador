package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.AlterarDeletarFornecedor;

public class AlterarDeletarFornecedorController {
    private Fornecedor fornecedorModel;
    private AlterarDeletarFornecedor view;

    public AlterarDeletarFornecedorController(AlterarDeletarFornecedor view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new Fornecedor(conexaoBanco);
    }

    public void atualizar() {
        if (fornecedorModel.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De Fornecedor Não Pode Ser Vazio");
            return;
        }

        Boolean result = fornecedorModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Fornecedor");
        }
    }

    public void deletar() {
        Boolean result = fornecedorModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Fornecedor");
        }
    }

    public void pesquisarNaReceita(String cnpj) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Ficar Vazio");
            return;
        }
        new RetornarFornecedor<>(view).execute("https://www.receitaws.com.br/v1/cnpj/", cnpj);
    }

    public void carregaFornecedor(String id) {
        fornecedorModel.load(id);
    }

    public Fornecedor getFornecedor() {
        return fornecedorModel;
    }
}
