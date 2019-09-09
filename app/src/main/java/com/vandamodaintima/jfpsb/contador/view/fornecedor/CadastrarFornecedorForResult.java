package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.content.Intent;

public class CadastrarFornecedorForResult extends CadastrarFornecedor {
    @Override
    public void aposCadastro(Object... args) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", controller.getFornecedor());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
