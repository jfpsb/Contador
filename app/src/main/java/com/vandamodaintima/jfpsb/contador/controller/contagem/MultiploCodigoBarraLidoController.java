package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.ArrayList;
import java.util.Date;

public class MultiploCodigoBarraLidoController {
    private ConexaoBanco conexaoBanco;
    private ProdutoGrade produtoGradeManager;
    private Contagem contagemManager;
    private ContagemProduto contagemProdutoManager;

    public MultiploCodigoBarraLidoController(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        produtoGradeManager = new ProdutoGrade(conexaoBanco);
        contagemManager = new Contagem(conexaoBanco);
        contagemProdutoManager = new ContagemProduto(conexaoBanco);
    }

    public ArrayList<ProdutoGrade> pesquisaProduto(String codigo) {
        ArrayList<ProdutoGrade> produtos = new ArrayList<>();

        //TODO: Arrumar
        /*if (!codigo.isEmpty()) {
            produtos = produtoGradeManager.listarPorId(codigo);
        }*/

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

    public void carregaProdutoGrade(String id) {
        contagemProdutoManager.setProdutoGrade(produtoGradeManager.listarPorId(id));
    }

    public void carregaProdutoGrade(Object o) {
        if (o instanceof ProdutoGrade)
            contagemProdutoManager.setProdutoGrade((ProdutoGrade) o);
    }
}
