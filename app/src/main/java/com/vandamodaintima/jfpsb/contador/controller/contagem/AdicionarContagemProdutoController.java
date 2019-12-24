package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;

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

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.getContagemProduto().setContagem(contagemManager.listarPorId(loja, data));
    }
}
