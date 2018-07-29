package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.app.Activity;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.FragmentContainer;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.AlterarFornecedorEmProduto;

/**
 * Activity host de AlterarFornecedorEmProduto
 */
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
