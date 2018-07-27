package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class CadastrarFornecedorSemInternetContainer extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_fragment_container_for_activity);
        stub.inflate();

        CadastrarFornecedorSemInternet cadastrarFornecedorSemInternet = new CadastrarFornecedorSemInternet();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, cadastrarFornecedorSemInternet);
        fragmentTransaction.commit();
    }

    public void setResultCadastro() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
