package com.vandamodaintima.jfpsb.contador.view.loja;

import android.os.Bundle;
import android.view.ViewStub;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

public class TelaLoja extends TabLayoutActivityBase {

    private CadastrarLoja telaCadastrarLoja;
    private PesquisarLoja telaPesquisarLoja;

    public TelaLoja() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        telaCadastrarLoja = new CadastrarLoja();
        telaPesquisarLoja = new PesquisarLoja();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setViewPagerTabLayout(telaPesquisarLoja, telaCadastrarLoja);
    }
}
