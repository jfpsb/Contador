package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.controller.produtocontagem.ProdutoContagemAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.dao.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class VisualizarProdutosContagem extends ActivityBase {
    private Contagem contagem;
    private ContagemProdutoManager contagemProdutoManager;
    private ListView listViewProdutoContagem;
    private ProdutoContagemAdapter produtoContagemCursorAdapter;

    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_visualizar_produtos_contagem);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        setListView();
    }

    @Override
    protected void setManagers() {
        contagemProdutoManager = new ContagemProdutoManager(conn);
    }

    private void setListView() {
        listViewProdutoContagem = findViewById(R.id.listViewProdutoContagem);

        try {
            cursor = contagemProdutoManager.listarPorContagemCursor(contagem);

            produtoContagemCursorAdapter = new ProdutoContagemAdapter(getApplicationContext(), cursor);

            listViewProdutoContagem.setAdapter(produtoContagemCursorAdapter);
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (cursor != null)
            cursor.close();

        super.onDestroy();
    }
}
