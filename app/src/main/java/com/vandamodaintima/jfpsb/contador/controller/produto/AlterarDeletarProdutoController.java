package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarProdutoController {
    AlterarDeletarView view;
    Context context;
    DAOProduto daoProduto;

    public AlterarDeletarProdutoController(AlterarDeletarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = view;
        this.context = context;
        daoProduto = new DAOProduto(sqLiteDatabase);
    }

    public void atualizar(Produto produto) {
        Boolean result = daoProduto.atualizar(produto, produto.getCod_barra());

        if(result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar(Produto produto) {
        Boolean result = daoProduto.deletar(produto.getCod_barra());

        if(result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }
}
