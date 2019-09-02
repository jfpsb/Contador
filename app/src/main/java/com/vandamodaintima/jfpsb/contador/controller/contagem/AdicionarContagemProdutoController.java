package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.database.Cursor;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.ArrayList;
import java.util.Date;

public class AdicionarContagemProdutoController {
    IAdicionarContagemProduto view;
    private ConexaoBanco conexaoBanco;
    private ProdutoModel produtoModel;
    private ContagemModel contagemModel;
    private ContagemProdutoModel contagemProdutoModel;

    public AdicionarContagemProdutoController(IAdicionarContagemProduto view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new ProdutoModel(conexaoBanco);
        contagemProdutoModel = new ContagemProdutoModel(conexaoBanco);
        contagemModel = new ContagemModel(conexaoBanco);
    }

    public void cadastrar(int quantidade) {
        contagemProdutoModel.setId(new Date().getTime());
        contagemProdutoModel.setProduto(produtoModel);
        contagemProdutoModel.setContagem(contagemModel);
        contagemProdutoModel.setQuant(quantidade);

        Boolean result = contagemProdutoModel.inserir();

        if (result) {
            view.mensagemAoUsuario("Contagem de Produto Adicionada Com Sucesso");
        } else {
            view.mensagemAoUsuario("Erro Ao Adicionar Contagem de Produto");
        }
    }

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public void carregaProduto(Object o) {
        if (o instanceof ProdutoModel)
            produtoModel = (ProdutoModel) o;
    }

    public void carregaContagem(String loja, String data) {
        contagemModel.load(loja, data);
        contagemProdutoModel.setContagem(contagemModel);
    }
}
