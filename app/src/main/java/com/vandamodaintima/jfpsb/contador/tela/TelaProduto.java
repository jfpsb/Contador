package com.vandamodaintima.jfpsb.contador.tela;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.excel.ManipulaExcel;
import com.vandamodaintima.jfpsb.contador.tela.manager.CadastrarProduto;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.manager.PesquisarProduto;

public class TelaProduto extends ActivityBase {

    private CadastrarProduto cadastrarProduto;
    private PesquisarProduto pesquisarProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_tela_produto);
        stub.inflate();

        cadastrarProduto = new CadastrarProduto();
        pesquisarProduto = new PesquisarProduto();

        setViewPagerTabLayout(pesquisarProduto, cadastrarProduto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cadastrar_produto_batch, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemCadastrarProdutosBatch:
                ManipulaExcel.adicionaProdutosDePlanilhaParaBD(getApplicationContext(), conn);
                //TODO: Colocar tela de progresso nesse cadastro
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
