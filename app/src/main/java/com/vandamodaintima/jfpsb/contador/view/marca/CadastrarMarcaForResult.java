package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.MarcaModel;

public class CadastrarMarcaForResult extends CadastrarMarca {
    @Override
    public void setAlertaCadastro(final MarcaModel marca) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar MarcaModel");

        String mensagem = "Deseja Cadastrar a MarcaModel " + marca.getNome() + "?";
        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean result = cadastrarMarcaController.cadastrar(marca);

                if(result) {
                    Intent intent = new Intent();
                    intent.putExtra("marca", marca);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        alertaCadastro.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("MarcaModel Não Foi Cadastrada");
            }
        });

        alertaCadastro.show();
    }
}
