package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.ArrayList;
import java.util.Date;

public class MultiploCodigoBarraLidoController {
    private ConexaoBanco conexaoBanco;
    private Produto produtoManager;
    private Contagem contagemManager;
    private ContagemProduto contagemProdutoManager;

    public MultiploCodigoBarraLidoController(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        produtoManager = new Produto(conexaoBanco);
        contagemManager = new Contagem(conexaoBanco);
        contagemProdutoManager = new ContagemProduto(conexaoBanco);
    }

    public ArrayList<Produto> pesquisaProduto(String codigo) {
        ArrayList<Produto> produtos = new ArrayList<>();

        if (!codigo.isEmpty()) {
            produtos = produtoManager.listarPorCodBarra(codigo);
        }

        return produtos;
    }

    public Boolean cadastrar() {
        contagemProdutoManager.setId(new Date().getTime());
        contagemProdutoManager.setQuant(1);
        return contagemProdutoManager.salvar();
    }

    public Boolean cadastrar(int quantidade) {
        contagemProdutoManager.setId(new Date().getTime());
        contagemProdutoManager.setQuant(quantidade);
        return contagemProdutoManager.salvar();
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
}
