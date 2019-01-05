package com.vandamodaintima.jfpsb.contador.view.fornecedor;

public class TelaFornecedorForResult extends TelaFornecedor {
    @Override
    public void setFragments() {
        cadastrarFornecedor = new CadastrarFornecedorForResult();
        pesquisarFornecedor = new PesquisarFornecedorForResult();
    }
}
