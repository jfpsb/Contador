package com.vandamodaintima.jfpsb.contador.view.produto;

import android.os.Bundle;

public class TelaProdutoForContagemForResult extends TelaProduto {
    @Override
    protected void setFragments() {
        String codigo = getIntent().getStringExtra("codigo");

        cadastrarProduto = new CadastrarProdutoForContagemForResult();
        pesquisarProduto = new PesquisarProdutoForContagemForResult();

        Bundle bundle = new Bundle();
        bundle.putString("codigo", codigo);

        cadastrarProduto.setArguments(bundle);
        pesquisarProduto.setArguments(bundle);
    }
}
