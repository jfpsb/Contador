package com.vandamodaintima.jfpsb.contador.view.marca;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.Marca;

public class CadastrarMarcaEmProduto extends CadastrarMarca {
    @Override
    protected void aposCadastro(Marca marca) {
        try {
            ((TelaMarcaForResult) (getActivity())).setResultado(marca);
        }
        catch (Exception e) {
            //Log.i(LOG, e.getMessage(), e);
        }
    }
}
