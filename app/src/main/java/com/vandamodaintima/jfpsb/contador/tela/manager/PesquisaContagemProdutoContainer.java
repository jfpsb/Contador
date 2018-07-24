package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

/**
 * PesquisaContagemProduto é uma Fragment filha de PesquisarProduto. Esta
 * activity serve como container de PesquisaContagemProduto
 */
public class PesquisaContagemProdutoContainer extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_pesquisa_contagem_produto_container);
        stub.inflate();

        PesquisaContagemProduto pesquisaContagemProduto = new PesquisaContagemProduto();

        FragmentManager fragmentManager = getSupportFragmentManager();


        Bundle bundle = new Bundle();
        bundle.putSerializable("contagem", getIntent().getExtras().getSerializable("contagem"));

        pesquisaContagemProduto.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, pesquisaContagemProduto);
        fragmentTransaction.commit();
    }
}
