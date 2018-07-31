package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class AdicionarMarcas extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_fornecedor);
        stub.inflate();
    }
}
