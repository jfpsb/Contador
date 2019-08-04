package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController {
    private CadastrarView view;
    private LojaModel lojaModel;

    public CadastrarLojaController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        lojaModel = new LojaModel(conexaoBanco);
    }

    public void cadastrar(LojaModel loja) {
        if (loja.getCnpj().isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (loja.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        Boolean result = loja.inserir();

        if (result) {
            view.mensagemAoUsuario("Loja Cadastrada Com Sucesso!");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Loja");
        }
    }

    public ArrayList<LojaModel> getMatrizes() {
        return lojaModel.listarMatrizes();
    }
}
