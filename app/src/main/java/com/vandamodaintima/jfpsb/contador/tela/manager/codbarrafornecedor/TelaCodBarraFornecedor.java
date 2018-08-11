package com.vandamodaintima.jfpsb.contador.tela.manager.codbarrafornecedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

public class TelaCodBarraFornecedor extends TabLayoutActivityBase {
    private CadastrarCodBarraFornecedor inserirCodBarraFornecedor;
    private PesquisarCodBarraFornecedor listarCodBarraFornecedor;

    private Bundle bundle;

    public TelaCodBarraFornecedor() {
        super(new String[] { "Listagem", "Inserção" });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_loja);
        stub.inflate();

        inserirCodBarraFornecedor = new CadastrarCodBarraFornecedor();
        listarCodBarraFornecedor = new PesquisarCodBarraFornecedor();

        bundle = new Bundle();
        bundle.putSerializable("produto", getIntent().getSerializableExtra("produto"));

        inserirCodBarraFornecedor.setArguments(bundle);
        listarCodBarraFornecedor.setArguments(bundle);

        setViewPagerTabLayout(listarCodBarraFornecedor, inserirCodBarraFornecedor);
    }

    @Override
    protected void setManagers() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        Produto produto = (Produto) bundle.getSerializable("produto");

        Intent intent = new Intent();

        intent.putExtra("codigos", produto.getCod_barra_fornecedor());

        setResult(RESULT_OK, intent);

        return super.onSupportNavigateUp();
    }
}
