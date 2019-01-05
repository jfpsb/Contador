package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

public class PesquisarFornecedorForResult extends PesquisarFornecedor {
    AlertDialog.Builder alertaEscolha;

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
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
                Intent intent = new Intent();
                intent.putExtra("fornecedor", fornecedor);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        alertaEscolha.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Fornecedor Não Foi Escolhido");
            }
        });

        alertaEscolha.show();
    }
}
