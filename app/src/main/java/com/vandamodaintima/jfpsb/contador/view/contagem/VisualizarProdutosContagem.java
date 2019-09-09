package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.VisualizarProdutosContagemController;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

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
