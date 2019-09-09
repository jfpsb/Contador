package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;

public class PesquisarFornecedorForResult extends PesquisarFornecedor {
    AlertDialog.Builder alertaEscolha;

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaFornecedor(id);
        setAlertaEscolha();
    }

    private void setAlertaEscolha() {
        alertaEscolha = new AlertDialog.Builder(getContext());
        alertaEscolha.setTitle("Escolher Fornecedor");
        alertaEscolha.setMessage("Tem Certeza Que Deseja Adicionar O Fornecedor " + controller.getFornecedor().getCnpj() + " - " + controller.getFornecedor().getNome() + " ao Produto?");

        alertaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("fornecedor", controller.getFornecedor());
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
