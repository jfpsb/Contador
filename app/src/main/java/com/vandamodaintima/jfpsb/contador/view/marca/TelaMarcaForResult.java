package com.vandamodaintima.jfpsb.contador.view.marca;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

public class TelaMarcaForResult extends TabLayoutActivityBase {
    private CadastrarMarca cadastrarMarca;
    private PesquisarMarca pesquisarMarca;

    public TelaMarcaForResult() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.contentStub);
        stub.setLayoutResource(R.layout.content_tela_tablayout);
        stub.inflate();

        cadastrarMarca = new CadastrarMarcaEmProduto();
        pesquisarMarca = new PesquisarMarcaEmProduto();

        setViewPagerTabLayout(pesquisarMarca, cadastrarMarca);
    }

    public void setResultado(Marca marca) {
        Intent intent = new Intent();
        intent.putExtra("marca", marca);
        setResult(RESULT_OK, intent);
        finish();
    }
}
