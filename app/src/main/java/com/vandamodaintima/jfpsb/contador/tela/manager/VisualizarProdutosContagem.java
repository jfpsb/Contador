package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ProdutoContagemCursorAdapter;
import com.vandamodaintima.jfpsb.contador.ProdutoCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;

public class VisualizarProdutosContagem extends AppCompatActivity {

    private ConexaoBanco conn;
    private Contagem contagem;
    private DAOContagemProduto daoContagemProduto;
    private ListView listViewProdutoContagem;
    private ProdutoContagemCursorAdapter produtoContagemCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_visualizar_produtos_contagem);
        stub.inflate();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conn = new ConexaoBanco(getApplicationContext());

        daoContagemProduto = new DAOContagemProduto(conn.conexao());

        setListView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        super.onDestroy();
    }

    private void setListView() {
        listViewProdutoContagem = findViewById(R.id.listViewProdutoContagem);

        Cursor cursor = daoContagemProduto.selectContagemProdutos(contagem.getIdcontagem());

        produtoContagemCursorAdapter = new ProdutoContagemCursorAdapter(getApplicationContext(),cursor);

        listViewProdutoContagem.setAdapter(produtoContagemCursorAdapter);
    }
}
