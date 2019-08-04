package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;

public class InserirCodBarraFornecedorController {
    private CadastrarView view;

    public InserirCodBarraFornecedorController(CadastrarView view) {
        this.view = view;
    }

    public void inserir(String cod_barra_fornecedor, ArrayList<String> codigos) {
        if (cod_barra_fornecedor.isEmpty()) {
            view.mensagemAoUsuario("Campo de Código de Barras de Fornecedor Não Pode Ser Vazio!");
            return;
        }

        if(codigos.contains(cod_barra_fornecedor)) {
            view.mensagemAoUsuario("Este Código Já Existe No Produto");
            view.limparCampos();
            return;
        }

        codigos.add(cod_barra_fornecedor);

        view.mensagemAoUsuario("Código de Barras de Fornecedor Adicionado à Lista");
        view.aposCadastro();
        view.limparCampos();
    }
}
