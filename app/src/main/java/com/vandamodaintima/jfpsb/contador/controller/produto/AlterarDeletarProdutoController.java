package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarProdutoController {
    AlterarDeletarView view;
    DAOProduto daoProduto;

    public AlterarDeletarProdutoController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase) {
        this.view = view;
        daoProduto = new DAOProduto(sqLiteDatabase);
    }

    public void atualizar(ProdutoModel produtoModel) {
        if (produtoModel.getDescricao().isEmpty()) {
            view.mensagemAoUsuario("Descrição do ProdutoModel Não Pode Ser Vazia");
            return;
        }

        if (produtoModel.getPreco() == 0) {
            view.mensagemAoUsuario("Preço do ProdutoModel Não Pode Ser Zero");
            return;
        }

        Boolean result = daoProduto.atualizar(produtoModel, produtoModel.getCod_barra());

        if (result) {
            view.mensagemAoUsuario("ProdutoModel Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar ProdutoModel");
        }
    }

    public void deletar(ProdutoModel produtoModel) {
        Boolean result = daoProduto.deletar(produtoModel.getCod_barra());

        if (result) {
            view.mensagemAoUsuario("ProdutoModel Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar ProdutoModel");
        }
    }
}
