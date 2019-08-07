package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;

public class PesquisarMarcaForResult extends PesquisarMarca {
    AlertDialog.Builder alertaEscolha;

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaMarca(id);
        setAlertaEscolha(controller.getNome());
    }

    private void setAlertaEscolha(final String nome) {
        alertaEscolha = new AlertDialog.Builder(getContext());
        alertaEscolha.setTitle("Escolher Marca");
        alertaEscolha.setMessage("Tem Certeza Que Deseja Adicionar A Marca " + nome + " ao Produto?");

        alertaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("marca", nome);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        alertaEscolha.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não Foi Escolhida");
            }
        });

        alertaEscolha.show();
    }
}
