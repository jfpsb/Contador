package com.vandamodaintima.jfpsb.contador.view.marca;

import android.os.Bundle;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class TelaMarca extends TabLayoutBaseView {

    protected CadastrarMarca cadastrarMarca;
    protected PesquisarMarca pesquisarMarca;

    public TelaMarca() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        setFragments();

        setViewPagerTabLayout(pesquisarMarca, cadastrarMarca);
    }

    protected void setFragments() {
        cadastrarMarca = new CadastrarMarca();
        pesquisarMarca = new PesquisarMarca();
    }
}
