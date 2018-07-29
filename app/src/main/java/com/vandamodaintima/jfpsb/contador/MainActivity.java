package com.vandamodaintima.jfpsb.contador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vandamodaintima.jfpsb.contador.tela.manager.contagem.TelaContador;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedor;
import com.vandamodaintima.jfpsb.contador.tela.manager.loja.TelaLoja;
import com.vandamodaintima.jfpsb.contador.tela.manager.produto.TelaProduto;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnContador = findViewById(R.id.btn_contador);
        Button btnProduto = findViewById(R.id.btn_produto);
        Button btnFornecedor = findViewById(R.id.btn_fornecedor);
        Button btnLoja = findViewById(R.id.btn_loja);



        btnContador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, TelaContador.class);
                startActivity(it);
            }
        });

        btnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, TelaProduto.class);
                startActivity(it);
            }
        });

        btnFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, TelaFornecedor.class);
                startActivity(it);
            }
        });

        btnLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, TelaLoja.class);
                startActivity(it);
            }
        });
    }
}
