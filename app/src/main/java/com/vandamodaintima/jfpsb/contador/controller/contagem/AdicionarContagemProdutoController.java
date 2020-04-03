package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;

public class AdicionarContagemProdutoController {
    IAdicionarContagemProduto view;
    private ConexaoBanco conexaoBanco;
    private Produto produtoModel;
    private Contagem contagemModel;
    private ContagemProduto contagemProdutoModel;

    public AdicionarContagemProdutoController(IAdicionarContagemProduto view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new Produto(conexaoBanco);
        contagemModel = new Contagem(conexaoBanco);
        contagemProdutoModel = new ContagemProduto(conexaoBanco);
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoModel.setContagem(contagemModel.listarPorId(loja, data));
    }
}
