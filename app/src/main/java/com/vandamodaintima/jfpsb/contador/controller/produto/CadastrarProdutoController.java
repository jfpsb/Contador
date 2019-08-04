package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

public class CadastrarProdutoController {

    CadastrarProduto view;
    ProdutoModel produtoModel;

    public CadastrarProdutoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarProduto) view;
        produtoModel = new ProdutoModel(conexaoBanco);
    }

    public void cadastrar(ProdutoModel produto) {
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

        if(produto.getFornecedor().getCnpj().isEmpty())
            produto.setFornecedor(null);

        if(produto.getMarca().getNome().isEmpty())
            produto.setMarca(null);

        Boolean result = produto.inserir();

        if (result) {
            view.mensagemAoUsuario("Produto Cadastro Com Sucesso");
            view.aposCadastro(produto.getCod_barra());
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            ProdutoModel produto = produtoModel.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }
}
