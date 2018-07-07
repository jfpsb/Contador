package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

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

        listViewProdutoContagem = findViewById(R.id.listViewProdutoContagem);

        setDAOs();

        setListView();
    }

    @Override
    protected void setDAOs() {
        daoContagemProduto = new DAOContagemProduto(conn.conexao());
    }

    private void setListView() {
        try {
            cursorLista = daoContagemProduto.selectContagemProdutos(contagem.getIdcontagem());

            produtoContagemCursorAdapter = new ProdutoContagemCursorAdapter(getApplicationContext(), cursorLista);

            listViewProdutoContagem.setAdapter(produtoContagemCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
