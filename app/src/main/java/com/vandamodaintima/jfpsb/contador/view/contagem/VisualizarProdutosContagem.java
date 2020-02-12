package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.VisualizarProdutosContagemController;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.produto.AlterarDeletarProduto;

public class VisualizarProdutosContagem extends ActivityBaseView {
    private ListView listViewProdutoContagem;

    private ConexaoBanco conexaoBanco;

    private VisualizarProdutosContagemController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_visualizar_produtos_contagem);
        stub.inflate();

        String loja = getIntent().getStringExtra("loja");
        String data = getIntent().getStringExtra("data");

        listViewProdutoContagem = findViewById(R.id.listViewProdutoContagem);

        listViewProdutoContagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("produto"));

                Intent alterarProduto = new Intent(VisualizarProdutosContagem.this, AlterarDeletarProduto.class);
                alterarProduto.putExtra("produto", cod_barra);

                startActivityForResult(alterarProduto, 1);
            }
        });

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new VisualizarProdutosContagemController(this, conexaoBanco);

        controller.carregaContagem(loja, data);
        controller.pesquisar();
    }

    public void setListViewAdaper(ListAdapter adapter) {
        listViewProdutoContagem.setAdapter(adapter);
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1) {
            controller.pesquisar();
        }
    }

    @Override
    protected void onDestroy() {
        ListAdapter listAdapter = listViewProdutoContagem.getAdapter();

        if (listAdapter instanceof CursorAdapter) {
            CursorAdapter cursorAdapter = (CursorAdapter) listAdapter;
            Cursor cursor = cursorAdapter.getCursor();
            if (cursor != null)
                cursor.close();
        }

        conexaoBanco.close();
        super.onDestroy();
    }
}
