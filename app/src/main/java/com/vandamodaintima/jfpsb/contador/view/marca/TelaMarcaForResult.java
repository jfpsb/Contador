package com.vandamodaintima.jfpsb.contador.view.marca;

public class TelaMarcaForResult extends TelaMarca {
    @Override
    protected void setFragments() {
        cadastrarMarca = new CadastrarMarcaForResult();
        pesquisarMarca = new PesquisarMarcaForResult();
    }
}
