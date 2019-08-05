package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;

public class CadastrarFornecedorForResult extends CadastrarFornecedor {
    @Override
    public void aposCadastro(Object... args) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", controller.getCnpj());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
