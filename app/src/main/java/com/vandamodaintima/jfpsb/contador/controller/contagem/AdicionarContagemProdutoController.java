package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;

import java.util.Date;

public class AdicionarContagemProdutoController {
    IAdicionarContagemProduto view;
    private ConexaoBanco conexaoBanco;
    private ProdutoManager produtoManager;
    private ContagemManager contagemManager;
    private ContagemProdutoManager contagemProdutoManager;

    public AdicionarContagemProdutoController(IAdicionarContagemProduto view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoManager = new ProdutoManager(conexaoBanco);
        contagemManager = new ContagemManager(conexaoBanco);
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);
    }

    public void cadastrar(int quantidade) {
        contagemProdutoManager.getContagemProduto().setId(new Date().getTime());
        contagemProdutoManager.getContagemProduto().setQuant(quantidade);

        Boolean result = contagemProdutoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void carregaProduto(String id) {
        contagemProdutoManager.getContagemProduto().setProduto(produtoManager.listarPorId(id));
    }

    public void carregaProduto(Object o) {
        if (o instanceof Produto)
            contagemProdutoManager.getContagemProduto().setProduto((Produto) o);
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.getContagemProduto().setContagem(contagemManager.listarPorId(loja, data));
    }
}
