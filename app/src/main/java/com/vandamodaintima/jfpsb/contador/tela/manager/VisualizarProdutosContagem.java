package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ListView;

import com.vandamodaintima.jfpsb.contador.ProdutoContagemCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public class VisualizarProdutosContagem extends ActivityBase {

    private Contagem contagem;
    private DAOContagemProduto daoContagemProduto;
    private ListView listViewProdutoContagem;
    private ProdutoContagemCursorAdapter produtoContagemCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_visualizar_produtos_contagem);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        daoContagemProduto = new DAOContagemProduto(conn.conexao());

        setListView();
    }

    private void setListView() {
        listViewProdutoContagem = findViewById(R.id.listViewProdutoContagem);

        Cursor cursor = daoContagemProduto.selectContagemProdutos(contagem.getIdcontagem());

        produtoContagemCursorAdapter = new ProdutoContagemCursorAdapter(getApplicationContext(),cursor);

        listViewProdutoContagem.setAdapter(produtoContagemCursorAdapter);
    }
}
