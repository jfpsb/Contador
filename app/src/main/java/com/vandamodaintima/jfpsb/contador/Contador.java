package com.vandamodaintima.jfpsb.contador;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.sincronizacao.Sincronizacao;
import com.vandamodaintima.jfpsb.contador.view.contagem.TelaContador;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedor;
import com.vandamodaintima.jfpsb.contador.view.loja.TelaLoja;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import java.util.ArrayList;
import java.util.List;

public class Contador extends AppCompatActivity {

    private static final int PERMISSOES_APP = 1;
    private ConexaoBanco conexaoBanco;
    private static Sincronizacao socketCliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador);

        Button btnContador = findViewById(R.id.btnContador);
        Button btnProduto = findViewById(R.id.btnProduto);
        Button btnFornecedor = findViewById(R.id.btnFornecedor);
        Button btnLoja = findViewById(R.id.btnLoja);

        btnContador.setOnClickListener(view -> {
            Intent it = new Intent(Contador.this, TelaContador.class);
            startActivity(it);
        });

        btnProduto.setOnClickListener(view -> {
            Intent it = new Intent(Contador.this, TelaProduto.class);
            startActivity(it);
        });

        btnFornecedor.setOnClickListener(view -> {
            Intent it = new Intent(Contador.this, TelaFornecedor.class);
            startActivity(it);
        });

        btnLoja.setOnClickListener(view -> {
            Intent it = new Intent(Contador.this, TelaLoja.class);
            startActivity(it);
        });

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        boolean permissaoRead = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        List<String> permissoes = new ArrayList<>();

        if (!permissaoRead) {
            permissoes.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!permissaoWrite) {
            permissoes.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissaoCamera) {
            permissoes.add(Manifest.permission.CAMERA);
        }

        if (permissoes.size() > 0)
            ActivityCompat.requestPermissions(this, permissoes.toArray(new String[0]), PERMISSOES_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        conexaoBanco = new ConexaoBanco(getApplicationContext());

        AndroidThreeTen.init(this);

        /*if (socketCliente == null) {
            socketCliente = new Sincronizacao(getApplicationContext(), conexaoBanco);
            socketCliente.start();
        }*/
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        //socketCliente.shutdownSocket();
        //socketCliente = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSOES_APP:
                if (grantResults.length > 0) {
                    for (int i : grantResults) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            switch (permissions[i]) {
                                case Manifest.permission.READ_EXTERNAL_STORAGE:
                                    Toast.makeText(this, "Permissão Concedida Para Ler De Armazenamento", Toast.LENGTH_SHORT).show();
                                    break;
                                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                    Toast.makeText(this, "Permissão Concedida Para Escrever Em Armazenamento", Toast.LENGTH_SHORT).show();
                                    break;
                                case Manifest.permission.CAMERA:
                                    Toast.makeText(this, "Permissão Concedida Para Usar Câmera", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                }
                break;
        }
    }
}
