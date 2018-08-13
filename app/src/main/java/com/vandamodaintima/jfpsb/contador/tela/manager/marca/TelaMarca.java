package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

public class TelaMarca extends TabLayoutActivityBase {

    private CadastrarMarca cadastrarMarca;
    private PesquisarMarca pesquisarMarca;

    public TelaMarca() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        cadastrarMarca = new CadastrarMarca();
        pesquisarMarca = new PesquisarMarca();

        setViewPagerTabLayout(pesquisarMarca, cadastrarMarca);
    }

    @Override
    protected void setManagers() {

    }
}
