package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;

public class CadastrarFornecedorSemInternetContainer extends FragmentContainer {

    public CadastrarFornecedorSemInternetContainer() {
        super(new CadastrarFornecedorSemInternet());
    }

    @Override
    public void setResultCadastro(Object object) {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
