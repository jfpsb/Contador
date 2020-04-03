package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Date;

public class PesquisarProdutoForContagemForResultController extends PesquisarProdutoController {
    private ContagemProduto contagemProdutoModel;

    public PesquisarProdutoForContagemForResultController(PesquisarView view, ConexaoBanco conexaoBanco) {
        super(view, conexaoBanco);
        contagemProdutoModel = new ContagemProduto(conexaoBanco);
    }

    public void atualizar() {
        Boolean result = produtoModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Foi Atualizado Com Código de Barras");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto Com Código de Barras");
        }
    }

    public void cadastrar(int quantidade) {
        contagemProdutoModel.setId(new Date().getTime());
        contagemProdutoModel.setQuant(quantidade);

        Boolean result = contagemProdutoModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }
}
