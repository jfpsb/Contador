package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

public class TelaContador extends TabLayoutActivityBase {

    private CadastrarContagem cadastrarContagem;
    private PesquisarContagem pesquisarContagem;

    public TelaContador() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        cadastrarContagem = new CadastrarContagem();
        pesquisarContagem = new PesquisarContagem();

        setViewPagerTabLayout(pesquisarContagem, cadastrarContagem);
    }
}
