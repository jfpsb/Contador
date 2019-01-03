package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

public class CadastrarProdutoController {

    CadastrarProduto view;
    DAOProduto daoProduto;

    public CadastrarProdutoController(CadastrarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = (CadastrarProduto) view;
        daoProduto = new DAOProduto(sqLiteDatabase);
    }

    public void cadastrar(Produto produto) {
        if (produto.getCod_barra().isEmpty()) {
            view.mensagemAoUsuario("Código de Barra Não Pode Estar Vazio");
            return;
        }

        if (produto.getDescricao().isEmpty()) {
            view.mensagemAoUsuario("Descrição Não Pode Estar Vazio");
            return;
        }

        if (produto.getPreco() == 0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero");
            return;
        }

        Boolean result = daoProduto.inserir(produto);

        if (result) {
            view.mensagemAoUsuario("Produto Cadastro Com Sucesso");
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            Produto produto = daoProduto.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }
}
