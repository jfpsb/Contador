package com.vandamodaintima.jfpsb.contador.tela.manager.loja;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.loja.CadastrarLoja;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.loja.PesquisarLoja;

public class TelaLoja extends ActivityBase {

    private CadastrarLoja telaCadastrarLoja;
    private PesquisarLoja telaPesquisarLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_loja);
        stub.inflate();

        telaCadastrarLoja = new CadastrarLoja();
        telaPesquisarLoja = new PesquisarLoja();

        setViewPagerTabLayout(telaPesquisarLoja, telaCadastrarLoja);
    }

    @Override
    protected void setManagers() {

    }
}
