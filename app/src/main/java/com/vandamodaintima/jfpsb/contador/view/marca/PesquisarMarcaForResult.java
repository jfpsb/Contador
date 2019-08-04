package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;

public class PesquisarMarcaForResult extends PesquisarMarca {
    AlertDialog.Builder alertaEscolha;

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        MarcaModel marca = new MarcaModel(conexaoBanco);

        marca.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
        marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

        setAlertaEscolha(marca);
    }

    private void setAlertaEscolha(final MarcaModel marca) {
        alertaEscolha = new AlertDialog.Builder(getContext());
        alertaEscolha.setTitle("Escolher Marca");
        alertaEscolha.setMessage("Tem Certeza Que Deseja Adicionar A Marca " + marca.getNome() + " ao Produto?");

        alertaEscolha.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("marca", marca);
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
