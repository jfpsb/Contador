package com.vandamodaintima.jfpsb.contador;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.sincronizacao.SocketCliente;
import com.vandamodaintima.jfpsb.contador.view.contagem.TelaContador;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedor;
import com.vandamodaintima.jfpsb.contador.view.loja.TelaLoja;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

public class Contador extends AppCompatActivity {

    private static final int PERMISSOES_APP = 1;
    private ConexaoBanco conexaoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_contador);

        Button btnContador = findViewById(R.id.btnContador);
        Button btnProduto = findViewById(R.id.btnProduto);
        Button btnFornecedor = findViewById(R.id.btnFornecedor);
        Button btnLoja = findViewById(R.id.btnLoja);

        btnContador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaContador.class);
                startActivity(it);
            }
        });

        btnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaProduto.class);
                startActivity(it);
            }
        });

        btnFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaFornecedor.class);
                startActivity(it);
            }
        });

        btnLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaLoja.class);
                startActivity(it);
            }
        });

        boolean permissaoRead = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

        if (!permissaoRead || !permissaoWrite || !permissaoCamera || !permissaoInternet)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSOES_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        conexaoBanco = new ConexaoBanco(getApplicationContext());
        new SocketCliente(getApplicationContext(), conexaoBanco).start();
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSOES_APP:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissões Concedidas", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
