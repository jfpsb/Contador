package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarContagem;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarContagem;

public class TelaContador extends ActivityBase {

    private CadastrarContagem cadastrarContagem;
    private PesquisarContagem pesquisarContagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_contador);
        stub.inflate();

        cadastrarContagem = new CadastrarContagem();
        pesquisarContagem = new PesquisarContagem();

        setViewPagerTabLayout(pesquisarContagem, cadastrarContagem);
    }
}
