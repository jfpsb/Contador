package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.app.Activity;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;

public class CadastrarFornecedorSemInternetContainer extends FragmentContainer {

    public CadastrarFornecedorSemInternetContainer() {
        super(new CadastrarFornecedorSemInternet());
    }

    @Override
    public void setResultCadastro(Object object) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", (Fornecedor) object);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
