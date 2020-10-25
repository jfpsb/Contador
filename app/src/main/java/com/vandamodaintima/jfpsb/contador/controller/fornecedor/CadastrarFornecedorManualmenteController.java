package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import com.google.gson.internal.$Gson$Preconditions;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarFornecedorManualmenteController implements IController {
    private ConexaoBanco conexaoBanco;
    private Fornecedor fornecedorModel;
    private CadastrarView view;

    public CadastrarFornecedorManualmenteController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        fornecedorModel = new Fornecedor(conexaoBanco);
    }

    public void salvar() {
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

    public Fornecedor getFornecedor() {
        return fornecedorModel;
    }

    @Override
    public void reset() {
        fornecedorModel = new Fornecedor(conexaoBanco);
    }
}
