package com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TelaCodBarraFornecedor extends TabLayoutBaseView {
    private InserirCodBarraFornecedor inserirCodBarraFornecedor;
    private ListarCodBarraFornecedor listarCodBarraFornecedor;
    private ArrayList<String> codigos;

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

        codigos = (ArrayList<String>) getIntent().getSerializableExtra("codigos");

        bundle = new Bundle();
        bundle.putSerializable("codigos", codigos);

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
        ArrayList<String> codigos = (ArrayList<String>) bundle.getSerializable("codigos");

        Intent intent = new Intent();
        intent.putExtra("codigos", codigos);

        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}
