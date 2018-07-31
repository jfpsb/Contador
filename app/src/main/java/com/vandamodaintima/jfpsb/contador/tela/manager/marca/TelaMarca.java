package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class TelaMarca extends ActivityBase {

    private CadastrarMarca cadastrarMarca;
    private PesquisarMarca pesquisarMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_loja);
        stub.inflate();

        cadastrarMarca = new CadastrarMarca();
        pesquisarMarca = new PesquisarMarca();

        setViewPagerTabLayout(pesquisarMarca, cadastrarMarca);
    }
}
