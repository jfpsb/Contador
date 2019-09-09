package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.ArrayList;
import java.util.Date;

public class TelaLerCodigoDeBarraController {
    private ITelaLerCodigoDeBarra view;
    private ProdutoManager produtoManager;
    private ContagemManager contagemManager;
    private ContagemProdutoManager contagemProdutoManager;

    public TelaLerCodigoDeBarraController(ITelaLerCodigoDeBarra view, ConexaoBanco conexaoBanco) {
        this.view = view;
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
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void cadastrar() {
        contagemProdutoManager.getContagemProduto().setId(new Date().getTime());
        contagemProdutoManager.getContagemProduto().setQuant(1);

        Boolean result = contagemProdutoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            view.realizarPesquisa();
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    ArrayList<Produto> pesquisarProduto(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

        if (!cod_barra.isEmpty()) {
            produtos = produtoManager.listarPorCodBarra(cod_barra);
        }

        return produtos;
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.getContagemProduto().setContagem(contagemManager.listarPorId(loja, data));
    }

    public void carregaProduto(String id) {
        contagemProdutoManager.getContagemProduto().setProduto(produtoManager.listarPorId(id));
    }

    public void carregaProduto(Object o) {
        if (o instanceof Produto)
            contagemProdutoManager.getContagemProduto().setProduto((Produto) o);
    }

    public Contagem getContagemManager() {
        return contagemProdutoManager.getContagemProduto().getContagem();
    }
}
