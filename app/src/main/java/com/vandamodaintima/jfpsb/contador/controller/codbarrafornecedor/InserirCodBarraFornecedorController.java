package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class InserirCodBarraFornecedorController {
    private CadastrarView view;

    public InserirCodBarraFornecedorController(CadastrarView view) {
        this.view = view;
    }

    public void inserir(String cod_barra_fornecedor, ProdutoModel produtoModel) {
        if (cod_barra_fornecedor.isEmpty()) {
            view.mensagemAoUsuario("Campo de Código de Barras de FornecedorModel Não Pode Ser Vazio!");
            return;
        }

        if(produtoModel.getCod_barra_fornecedor().contains(cod_barra_fornecedor)) {
            view.mensagemAoUsuario("Este Código Já Existe No ProdutoModel");
            view.limparCampos();
            return;
        }

        produtoModel.getCod_barra_fornecedor().add(cod_barra_fornecedor);

        view.mensagemAoUsuario("Código de Barras de FornecedorModel Adicionado à Lista");
        view.aposCadastro();
        view.limparCampos();
    }
}
