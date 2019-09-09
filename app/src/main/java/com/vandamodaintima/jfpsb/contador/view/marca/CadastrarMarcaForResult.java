package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.content.Intent;

public class CadastrarMarcaForResult extends CadastrarMarca {
    @Override
    public void aposCadastro(Object... args) {
        Intent intent = new Intent();
        intent.putExtra("marca", controller.getMarca());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
