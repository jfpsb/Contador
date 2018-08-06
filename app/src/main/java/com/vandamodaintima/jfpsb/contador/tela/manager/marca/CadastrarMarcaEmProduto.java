package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Marca;

public class CadastrarMarcaEmProduto extends CadastrarMarca {
    @Override
    protected void aposCadastro(Marca marca) {
        try {
            ((TelaMarcaForResult) (getActivity())).setResultado(marca);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage(), e);
        }
    }
}
