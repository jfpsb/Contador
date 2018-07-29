package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.os.Bundle;

import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;

import org.apache.poi.ss.formula.eval.NotImplementedException;

/**
 * PesquisaContagemProduto é uma Fragment filha de PesquisarProduto. Esta
 * activity serve como container de PesquisaContagemProduto
 */
public class PesquisaContagemProdutoContainer extends FragmentContainer {

    public PesquisaContagemProdutoContainer() {
        super(new PesquisaContagemProduto());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PesquisaContagemProduto pesquisaContagemProduto = (PesquisaContagemProduto) getFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable("contagem", getIntent().getExtras().getSerializable("contagem"));

        pesquisaContagemProduto.setArguments(bundle);
    }

    @Override
    public void setResultCadastro(Object object) {
        throw new NotImplementedException("Você Não Devia Usar Esse Método");
    }
}
