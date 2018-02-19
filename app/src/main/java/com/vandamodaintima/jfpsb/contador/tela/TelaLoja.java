package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarLoja;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarLoja;

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

        telaCadastrarLoja.setConn(conn);
        telaPesquisarLoja.setConn(conn);

        setViewPagerTabLayout(telaPesquisarLoja, telaCadastrarLoja);
    }
}
