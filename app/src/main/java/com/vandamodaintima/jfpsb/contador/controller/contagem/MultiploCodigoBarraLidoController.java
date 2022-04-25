package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultiploCodigoBarraLidoController {
    private ConexaoBanco conexaoBanco;
    private ProdutoGrade produtoGrade;
    private Produto produto;
    private Contagem contagemManager;
    private ContagemProduto contagemProdutoManager;

    public MultiploCodigoBarraLidoController(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        produto = new Produto(conexaoBanco);
        produtoGrade = new ProdutoGrade(conexaoBanco);
        contagemManager = new Contagem(conexaoBanco);
        contagemProdutoManager = new ContagemProduto(conexaoBanco);
    }

    public List<ProdutoGrade> pesquisaProdutoGrade(String codigo) {
        List<ProdutoGrade> produtoGrades = new ArrayList<>();

        if (!codigo.isEmpty()) {
            produtoGrades = produtoGrade.listarPorCodBarra(codigo);
        }

        return produtoGrades;
    }

    public Produto pesquisaProduto(String codigo) {
        return produto.listarPorId(codigo);
    }

    public Boolean cadastrar() {
        contagemProdutoManager.setQuant(1);
        return contagemProdutoManager.salvar();
    }

    public Boolean cadastrar(int quantidade) {
        contagemProdutoManager.setQuant(quantidade);
        return contagemProdutoManager.salvar();
    }

    public void carregaContagem(String loja, String data) {
        contagemProdutoManager.setContagem(contagemManager.listarPorId(loja, data));
    }

    public void carregaProdutoGrade(ProdutoGrade produtoGrade) {
        contagemProdutoManager.setProdutoGrade(produtoGrade);
    }
}
