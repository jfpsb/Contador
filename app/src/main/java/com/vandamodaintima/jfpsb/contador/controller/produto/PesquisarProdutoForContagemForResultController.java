package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

import java.util.Date;

public class PesquisarProdutoForContagemForResultController extends PesquisarProdutoController {
    private ContagemProdutoManager contagemProdutoManager;

    public PesquisarProdutoForContagemForResultController(PesquisarView view, ConexaoBanco conexaoBanco) {
        super(view, conexaoBanco);
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);
    }

    public void atualizar() {
        Boolean result = produtoManager.atualizar(produtoManager.getProduto().getCod_barra());

        if (result) {
            view.mensagemAoUsuario("Produto Foi Atualizado Com Código de Barras");
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto Com Código de Barras");
        }
    }

    public void cadastrar(int quantidade) {
        contagemProdutoManager.getContagemProduto().setId(new Date().getTime());
        contagemProdutoManager.getContagemProduto().setQuant(quantidade);

        Boolean result = contagemProdutoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }
}
