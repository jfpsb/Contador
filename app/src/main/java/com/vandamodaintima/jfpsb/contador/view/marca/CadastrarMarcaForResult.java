package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.Marca;

public class CadastrarMarcaForResult extends CadastrarMarca {
    @Override
    public void setAlertaCadastro(final Marca marca) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Marca");

        String mensagem = "Deseja Cadastrar a Marca " + marca.getNome() + "?";
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
                mensagemAoUsuario("Marca Não Foi Cadastrada");
            }
        });

        alertaCadastro.show();
    }
}
