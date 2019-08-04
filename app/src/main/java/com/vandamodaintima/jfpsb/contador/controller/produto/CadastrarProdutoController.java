package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

public class CadastrarProdutoController {

    CadastrarProduto view;
    DAOProduto daoProduto;

    public CadastrarProdutoController(CadastrarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = (CadastrarProduto) view;
        daoProduto = new DAOProduto(sqLiteDatabase);
    }

    public void cadastrar(ProdutoModel produtoModel) {
        if (produtoModel.getCod_barra().isEmpty()) {
            view.mensagemAoUsuario("Código de Barra Não Pode Estar Vazio");
            return;
        }

        if (produtoModel.getDescricao().isEmpty()) {
            view.mensagemAoUsuario("Descrição Não Pode Estar Vazio");
            return;
        }

        if (produtoModel.getPreco() == 0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero");
            return;
        }

        Boolean result = daoProduto.inserir(produtoModel);

        if (result) {
            view.mensagemAoUsuario("ProdutoModel Cadastro Com Sucesso");
            view.aposCadastro(produtoModel);
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("ProdutoModel Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            ProdutoModel produtoModel = daoProduto.listarPorId(codigo);

            if (produtoModel != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }
}
