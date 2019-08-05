package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;

public class CadastrarMarcaForResult extends CadastrarMarca {
    @Override
    public void aposCadastro(Object... args) {
        Intent intent = new Intent();
        intent.putExtra("marca", controller.getNome());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
