package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

public class TelaCodBarraFornecedor extends TabLayoutActivityBase {
    private CadastrarCodBarraFornecedor inserirCodBarraFornecedor;
    private ListarCodBarraFornecedor listarCodBarraFornecedor;

    private Bundle bundle;

    public TelaCodBarraFornecedor() {
        super(new String[] { "Listagem", "Inserção" });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        inserirCodBarraFornecedor = new CadastrarCodBarraFornecedor();
        listarCodBarraFornecedor = new ListarCodBarraFornecedor();

        Produto produto = (Produto) getIntent().getSerializableExtra("produto");

        bundle = new Bundle();
        bundle.putSerializable("produto", produto);

//        inserirCodBarraFornecedor.setArguments(bundle);
//        listarCodBarraFornecedor.setArguments(bundle);
//
//        setViewPagerTabLayout(listarCodBarraFornecedor, inserirCodBarraFornecedor);
    }

    @Override
    public void onBackPressed() {
        Produto produto = (Produto) bundle.getSerializable("produto");

        Intent intent = new Intent();
        intent.putExtra("produto", produto);

        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}