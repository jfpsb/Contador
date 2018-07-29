package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;

public class CadastrarFornecedorEmProduto extends CadastrarFornecedor {
    @Override
    protected void aposCadastro(Fornecedor fornecedor) {
        try {
            ((TelaFornecedorForResult) (getActivity())).setResultado(fornecedor);
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage(), e);
        }
    }
}
