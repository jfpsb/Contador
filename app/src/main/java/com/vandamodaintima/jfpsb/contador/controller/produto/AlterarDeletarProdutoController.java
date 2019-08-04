package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarProdutoController {
    AlterarDeletarView view;

    public AlterarDeletarProdutoController(AlterarDeletarView view) {
        this.view = view;
    }

    public void atualizar(ProdutoModel produto) {
        if (produto.getDescricao().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        if (produto.getPreco() == 0) {
            view.mensagemAoUsuario("Preço do Produto Não Pode Ser Zero");
            return;
        }

        Boolean result = produto.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar(ProdutoModel produto) {
        Boolean result = produto.deletar();

        if (result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }
}
