package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarFornecedorController {
    private FornecedorModel fornecedorModel;
    private AlterarDeletarView view;

    public AlterarDeletarFornecedorController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public void atualizar(String nome, String fantasia, String email) {
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome De Fornecedor Não Pode Ser Vazio");
            return;
        }

        fornecedorModel.setNome(nome);
        fornecedorModel.setFantasia(fantasia);
        fornecedorModel.setEmail(email);

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

    public void carregaFornecedor(String id) {
        fornecedorModel.load(id);
    }

    public String getCnpj() {
        return fornecedorModel.getCnpj();
    }

    public String getNome() {
        return fornecedorModel.getNome();
    }

    public String getFantasia() {
        return fornecedorModel.getFantasia();
    }

    public String getEmail() {
        return fornecedorModel.getEmail();
    }
}
