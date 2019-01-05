package com.vandamodaintima.jfpsb.contador.view.marca;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

public class TelaMarca extends TabLayoutActivityBase {

    protected CadastrarMarca cadastrarMarca;
    protected PesquisarMarca pesquisarMarca;

    public TelaMarca() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        setFragments();

        setViewPagerTabLayout(pesquisarMarca, cadastrarMarca);
    }

    protected void setFragments() {
        cadastrarMarca = new CadastrarMarca();
        pesquisarMarca = new PesquisarMarca();
    }
}
