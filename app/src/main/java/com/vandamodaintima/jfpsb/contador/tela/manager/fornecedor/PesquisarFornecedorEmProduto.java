package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;

/**
 * Usado em TelaFornecedorForResult
 * Lista os fornecedores e permite que o usuário escolha um ao clicar nele
 * e atribuir ao produto
 * Isso é uma Fragment
 */
public class PesquisarFornecedorEmProduto extends PesquisarFornecedor {
    AlertDialog.Builder alertaEscolha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));

                setAlertaEscolha(fornecedor);
            }
        });
    }

    private void setAlertaEscolha(final Fornecedor fornecedor) {
        alertaEscolha = new AlertDialog.Builder(getContext());
        alertaEscolha.setTitle("Escolher Fornecedor");
        alertaEscolha.setMessage("Tem Certeza Que Deseja Adicionar O Fornecedor " + fornecedor.getCnpj() + " - " + fornecedor.getNome() + " ao Produto?");

        alertaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TelaFornecedorForResult) getActivity()).setResultado(fornecedor);
            }
        });

        alertaEscolha.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Fornecedor Não Foi Escolhido", Toast.LENGTH_SHORT).show();
            }
        });

        alertaEscolha.show();
    }
}
