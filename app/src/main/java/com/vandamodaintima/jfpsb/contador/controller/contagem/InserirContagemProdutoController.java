package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.DataSetObserver;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.ArrayList;
import java.util.List;

public class InserirContagemProdutoController {
    private CadastrarView view;
    private ProdutoGrade produtoGrade;
    private Contagem contagem;
    private ContagemProduto model;
    private ConexaoBanco conexaoBanco;
    private ContagemProdutoArrayAdapter contagemProdutoArrayAdapter;

    public InserirContagemProdutoController(CadastrarView view, ConexaoBanco conexaoBanco, String contagemId) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagem = new Contagem(conexaoBanco);
        model = new ContagemProduto(conexaoBanco);
        produtoGrade = new ProdutoGrade(conexaoBanco);

        contagem.load(contagemId);
        model.setContagem(contagem);
        contagemProdutoArrayAdapter = new ContagemProdutoArrayAdapter(view.getContext(), R.layout.item_produto_contagem_com_grade, model.listarPorContagem(contagem));
        view.setListViewAdapter(contagemProdutoArrayAdapter);
    }

    public void cadastrar(int quantidade) {
        model.setQuant(quantidade);
        Boolean result = model.salvar();
        if (result) {
            contagemProdutoArrayAdapter.add(model);
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            model = new ContagemProduto(conexaoBanco);
            model.setContagem(contagem);
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void cadastrar() {
        model.setQuant(1);
        Boolean result = model.salvar();
        if (result) {
            contagemProdutoArrayAdapter.add(model);
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
            model = new ContagemProduto(conexaoBanco);
            model.setContagem(contagem);
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

    public void carregaProdutoGrade(ProdutoGrade produtoGrade) {
        model.setProdutoGrade(produtoGrade);
    }

    public Contagem getContagem() {
        return model.getContagem();
    }
}
