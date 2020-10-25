package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewStub;
import android.view.WindowManager;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

import java.io.Serializable;

public class GerenciarProdutoGrades extends TabLayoutBaseView {
    private InserirProdutoGrade inserirProdutoGrade;

    public GerenciarProdutoGrades() {
        super(new String[]{"Inserir", "Visualizar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        ViewStub stub2 = findViewById(R.id.telaLayoutViewStub);
        stub2.setLayoutResource(R.layout.activity_tela_tablayout);
        stub2.inflate();

        inserirProdutoGrade = new InserirProdutoGrade();
        VisualizarProdutoGrades visualizarProdutoGrades = new VisualizarProdutoGrades();

        inserirProdutoGrade.setArguments(getIntent().getExtras());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setViewPagerTabLayout(inserirProdutoGrade, visualizarProdutoGrades);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("produtoGrades", (Serializable) inserirProdutoGrade.getProdutoGrades());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
