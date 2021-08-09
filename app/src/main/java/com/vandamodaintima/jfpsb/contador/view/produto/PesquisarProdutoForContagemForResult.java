package com.vandamodaintima.jfpsb.contador.view.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.grade.ProdutoGradeArrayAdapter;
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoForContagemForResultController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;

import java.util.Objects;

public class PesquisarProdutoForContagemForResult extends PesquisarProduto {
    private String codigo;
    private AlertDialog.Builder alertaQuantidadeProduto;
    private AlertDialog.Builder alertaProdutoGrade;
    private ProdutoGradeArrayAdapter produtoGradeArrayAdapter;

    private static final int TELA_SELECIONAR_PRODUTO = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //Código de barras usado na pesquisa inicial
        codigo = getArguments().getString("codigo");
        controller = new PesquisarProdutoForContagemForResultController(this, conexaoBanco);
        return view;
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaProduto(id);
        controller.setProdutoGradeModel(null);

        if (controller.getProduto().getProdutoGrades().size() > 0) {
            produtoGradeArrayAdapter = new ProdutoGradeArrayAdapter(Objects.requireNonNull(getContext()), controller.getProduto().getProdutoGrades(), false);
            setAlertaProdutoGrade();
            alertaProdutoGrade.show();
        } else {
            setAlertaQuantidadeProduto();
            alertaQuantidadeProduto.show();
        }
    }

    private void setAlertaQuantidadeProduto() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = view.findViewById(R.id.txtQuantidade);

        alertaQuantidadeProduto = new AlertDialog.Builder(getActivity());
        alertaQuantidadeProduto.setView(view);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String txtQuant = txtQuantidade.getText().toString();
            int quantidade = 1;

            if (!txtQuant.isEmpty()) {
                quantidade = Integer.parseInt(txtQuant);
            }

            if (quantidade < 1) {
                mensagemAoUsuario("Informe Uma Quantidade Válida");
                return;
            }

            //((PesquisarProdutoForContagemForResultController) controller).atualizar();

            Intent intent = new Intent();

            if (controller.getProdutoGradeModel() != null) {
                intent.putExtra("produtograde", controller.getProdutoGradeModel());
            } else {
                intent.putExtra("produto", controller.getProduto());
            }

            intent.putExtra("quantidade", quantidade);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", (dialogInterface, i) -> mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada"));
    }

    private void setAlertaProdutoGrade() {
        alertaProdutoGrade = new AlertDialog.Builder(getContext());
        alertaProdutoGrade.setTitle("Selecione A Grade:");
        alertaProdutoGrade.setAdapter(produtoGradeArrayAdapter, (dialog, which) -> {
            ProdutoGrade pg = produtoGradeArrayAdapter.getItem(which);
            Toast.makeText(getContext(), "Grade Selecionada: " + pg.getCodBarra(), Toast.LENGTH_LONG).show();
            controller.setProdutoGradeModel(pg);
            setAlertaQuantidadeProduto();
            alertaQuantidadeProduto.show();
        });
        alertaProdutoGrade.setNegativeButton("Selecionar Produto E Não Grade", (dialog, which) -> {
            controller.setProdutoGradeModel(null);
            setAlertaQuantidadeProduto();
            alertaQuantidadeProduto.show();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_SELECIONAR_PRODUTO) {
            if (resultCode == Activity.RESULT_OK) {
                Produto produto = (Produto) data.getSerializableExtra("produto");
                controller.carregaProduto(produto);
                int quantidade = data.getIntExtra("quantidade", 1);
                ((PesquisarProdutoForContagemForResultController) controller).cadastrar(quantidade);
            }
        }
    }
}
