package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;

public class AlterarFornecedorEmProdutoContainer extends FragmentContainer {

    public AlterarFornecedorEmProdutoContainer() {
        super(new AlterarFornecedorEmProduto());
    }

    @Override
    public void setResultCadastro(Object object) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", (Fornecedor)object);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
