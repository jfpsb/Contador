package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarFornecedor;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarFornecedor;
import com.vandamodaintima.jfpsb.contador.R;

public class TelaFornecedor extends ActivityBase {

    private CadastrarFornecedor cadastrarFornecedor;
    private PesquisarFornecedor pesquisarFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_fornecedor);
        stub.inflate();

        cadastrarFornecedor = new CadastrarFornecedor();
        pesquisarFornecedor = new PesquisarFornecedor();

        setViewPagerTabLayout(pesquisarFornecedor, cadastrarFornecedor);
    }
}
