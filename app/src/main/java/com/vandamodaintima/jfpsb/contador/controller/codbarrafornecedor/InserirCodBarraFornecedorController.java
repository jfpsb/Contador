package com.vandamodaintima.jfpsb.contador.controller.codbarrafornecedor;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class InserirCodBarraFornecedorController {
    private CadastrarView view;

    public InserirCodBarraFornecedorController(CadastrarView view) {
        this.view = view;
    }

    public void inserir(String cod_barra_fornecedor, Produto produto) {
        if (cod_barra_fornecedor.isEmpty()) {
            view.mensagemAoUsuario("Campo de Código de Barras de Fornecedor Não Pode Ser Vazio!");
            return;
        }

        if(produto.getCod_barra_fornecedor().contains(cod_barra_fornecedor)) {
            view.mensagemAoUsuario("Este Código Já Existe No Produto");
            view.limparCampos();
            return;
        }

        produto.getCod_barra_fornecedor().add(cod_barra_fornecedor);

        view.mensagemAoUsuario("Código de Barras de Fornecedor Adicionado à Lista");
        view.aposCadastro();
        view.limparCampos();
    }
}
