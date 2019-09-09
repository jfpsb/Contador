package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;

import java.util.ArrayList;
import java.util.Date;

public class MultiploCodigoBarraLidoController {
    private ConexaoBanco conexaoBanco;
    private ProdutoManager produtoManager;
    private ContagemManager contagemManager;
    ContagemProdutoManager contagemProdutoManager;

    public MultiploCodigoBarraLidoController(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        produtoManager = new ProdutoManager(conexaoBanco);
        contagemManager = new ContagemManager(conexaoBanco);
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);
    }

    public ArrayList<Produto> pesquisaProduto(String codigo) {
        ArrayList<Produto> produtos = new ArrayList<>();

        if (!codigo.isEmpty()) {
            produtos = produtoManager.listarPorCodBarra(codigo);
        }

        return produtos;
    }

    public Boolean cadastrar() {
        contagemProdutoManager.getContagemProduto().setId(new Date().getTime());
        contagemProdutoManager.getContagemProduto().setQuant(1);
        return contagemProdutoManager.salvar();
    }

    public Boolean cadastrar(int quantidade) {
        contagemProdutoManager.getContagemProduto().setId(new Date().getTime());
        contagemProdutoManager.getContagemProduto().setQuant(quantidade);
        return contagemProdutoManager.salvar();
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
}
