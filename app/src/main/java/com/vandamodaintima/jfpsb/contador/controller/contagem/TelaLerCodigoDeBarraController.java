package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.contagem.TelaLerCodigoDeBarraContagemProduto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TelaLerCodigoDeBarraController {
    private TelaLerCodigoDeBarraContagemProduto view;
    private Produto produto;
    private ProdutoGrade produtoGrade;
    private Contagem contagemManager;
    private ContagemProduto model;
    private ConexaoBanco conexaoBanco;

    public TelaLerCodigoDeBarraController(TelaLerCodigoDeBarraContagemProduto view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produto = new Produto(conexaoBanco);
        contagemManager = new Contagem(conexaoBanco);
        model = new ContagemProduto(conexaoBanco);
        produtoGrade = new ProdutoGrade(conexaoBanco);
    }

    public void cadastrar(int quantidade) {
        model.setId(new Date().getTime());
        model.setQuant(quantidade);

        Boolean result = model.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            model = new ContagemProduto(conexaoBanco);
            model.setContagem(contagemManager);
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void cadastrar() {
        model.setId(new Date().getTime());
        model.setQuant(1);

        Boolean result = model.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public List<ProdutoGrade> pesquisarProdutoGrade(String cod_barra) {
        List<ProdutoGrade> produtoGrades = new ArrayList<>();

        if (!cod_barra.trim().isEmpty()) {
            produtoGrades = produtoGrade.listarPorCodBarra(cod_barra);
        }

        return produtoGrades;
    }

    public Produto pesquisarProduto(String codigo) {
        return produto.listarPorId(codigo);
    }

    public void carregaContagem(long id) {
        contagemManager.load(id);
        model.setContagem(contagemManager);
    }

    public void carregaProdutoGrade(ProdutoGrade produtoGrade) {
        model.setProdutoGrade(produtoGrade);
    }

    public void carregaProduto(Produto produto) {
        model.setProduto(produto);
    }

    public Contagem getContagem() {
        return model.getContagem();
    }
}
