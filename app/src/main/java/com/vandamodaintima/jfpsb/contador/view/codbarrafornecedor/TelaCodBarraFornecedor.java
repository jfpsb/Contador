package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

public class TelaCodBarraFornecedor extends TabLayoutActivityBase {
    private InserirCodBarraFornecedor inserirCodBarraFornecedor;
    private ListarCodBarraFornecedor listarCodBarraFornecedor;

    private Bundle bundle;

    public TelaCodBarraFornecedor() {
        super(new String[]{"Listagem", "Inserção"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        inserirCodBarraFornecedor = new InserirCodBarraFornecedor();
        listarCodBarraFornecedor = new ListarCodBarraFornecedor();

        Produto produto = (Produto) getIntent().getSerializableExtra("produto");

        bundle = new Bundle();
        bundle.putSerializable("produto", produto);

        inserirCodBarraFornecedor.setArguments(bundle);
        listarCodBarraFornecedor.setArguments(bundle);

        View view = findViewById(R.id.tela_tablayout);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }

                return false;
            }
        });

        setViewPagerTabLayout(listarCodBarraFornecedor, inserirCodBarraFornecedor);
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
