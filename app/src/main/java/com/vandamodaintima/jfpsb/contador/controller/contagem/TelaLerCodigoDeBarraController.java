package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.ArrayList;
import java.util.Date;

public class TelaLerCodigoDeBarraController {
    private ITelaLerCodigoDeBarra view;
    private Produto produtoManager;
    private Contagem contagemManager;
    private ContagemProduto contagemProdutoManager;

    public TelaLerCodigoDeBarraController(ITelaLerCodigoDeBarra view, ConexaoBanco conexaoBanco) {
        this.view = view;
        produtoManager = new Produto(conexaoBanco);
        contagemManager = new Contagem(conexaoBanco);
        contagemProdutoManager = new ContagemProduto(conexaoBanco);
    }

    public void cadastrar(int quantidade) {
        contagemProdutoManager.setId(new Date().getTime());
        contagemProdutoManager.setQuant(quantidade);

        Boolean result = contagemProdutoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void cadastrar() {
        contagemProdutoManager.setId(new Date().getTime());
        contagemProdutoManager.setQuant(1);

        Boolean result = contagemProdutoManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public ArrayList<Produto> pesquisarProduto(String cod_barra) {
        ArrayList<Produto> produtos = new ArrayList<>();

        if (!cod_barra.isEmpty()) {
            produtos = produtoManager.listarPorCodBarra(cod_barra);
        }

        return produtos;
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.setContagem(contagemManager.listarPorId(loja, data));
    }

    public void carregaProduto(String id) {
        contagemProdutoManager.setProduto(produtoManager.listarPorId(id));
    }

    public void carregaProduto(Object o) {
        if (o instanceof Produto)
            contagemProdutoManager.setProduto((Produto) o);
    }

    public Contagem getContagem() {
        return contagemProdutoManager.getContagem();
    }
}
