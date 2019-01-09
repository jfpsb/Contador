package com.vandamodaintima.jfpsb.contador.view.produto;

public class TelaProdutoForContagemForResult extends TelaProduto {
    @Override
    protected void setFragments() {
        cadastrarProduto = new CadastrarProdutoForContagemForResult();
        pesquisarProduto = new PesquisarProdutoForContagemForResult();
    }
}
