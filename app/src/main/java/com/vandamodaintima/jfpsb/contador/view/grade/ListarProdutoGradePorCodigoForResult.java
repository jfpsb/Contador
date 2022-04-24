package com.vandamodaintima.jfpsb.contador.view.grade;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.grade.ListarProdutoGradePorCodigoController;
import com.vandamodaintima.jfpsb.contador.controller.grade.ProdutoGradeArrayAdapter;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

/***
 * Lista as grades de produto encontradas baseado em um código de barras comum entre elas.
 * Retorna a grade selecionada pelo usuário ao tocar no item da lista.
 * O código a ser pesquisado deve ser passado através da Intent com nome "codigo".
 */
public class ListarProdutoGradePorCodigoForResult extends ActivityBaseView {
    private Button btnGradeNaoEncontrada;
    private ListView listView;
    private ConexaoBanco conexaoBanco;
    private ListarProdutoGradePorCodigoController controller;
    private String codigo;
    private ProdutoGradeArrayAdapter produtoGradeArrayAdapter;

    private static final int PESQUISA_PRODUTO = 1;

    protected AlertDialog.Builder alertBuilderDeletar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_listar_produto_grade_por_codigo);
        stub.inflate();

        codigo = getIntent().getStringExtra("codigo");

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new ListarProdutoGradePorCodigoController(conexaoBanco);

        btnGradeNaoEncontrada = findViewById(R.id.btnGradeNaoEncontrada);
        listView = findViewById(R.id.listViewProdutoGrade);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ProdutoGrade produtoGrade = (ProdutoGrade) parent.getItemAtPosition(position);
            controller.setProdutoGrade(produtoGrade);
            alertBuilderDeletar.show();
        });

        btnGradeNaoEncontrada.setOnClickListener(v -> {
            //TODO: abrir tela de pesquisa/cadastro de produto For Result, mas irá retornar um produto_grade
        });

        produtoGradeArrayAdapter = new ProdutoGradeArrayAdapter(getApplicationContext(), controller.getProdutoGrades(codigo), true);
        listView.setAdapter(produtoGradeArrayAdapter);

        setAlertBuilderDeletar();
    }

    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Selecionar Grade");
        alertBuilderDeletar.setMessage(String.format("Tem Certeza Que Deseja Escolher a Grade '%s'?", controller.getProdutoGrade().getGradesToShortString()));

        alertBuilderDeletar.setPositiveButton("Sim", (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.putExtra("produto_grade", controller.getProdutoGrade());
            setResult(RESULT_OK, intent);
            finish();
        });

        alertBuilderDeletar.setNegativeButton("Não", (dialogInterface, i) -> Toast.makeText(getApplicationContext(), "Grade Não Foi Selecionada", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PESQUISA_PRODUTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Selecione a Grade Inserida na Lista", Toast.LENGTH_SHORT).show();
                produtoGradeArrayAdapter.clear();
                produtoGradeArrayAdapter.addAll(controller.getProdutoGrades(codigo));
                produtoGradeArrayAdapter.notifyDataSetChanged();
            }
        }
    }
}
