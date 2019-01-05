package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

public class CadastrarFornecedorForResult extends CadastrarFornecedor {
    @Override
    public void setAlertaCadastro(final Fornecedor fornecedor) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Fornecedor");

        String mensagem = "Confirme os Dados do Fornecedor Encontrado Com CNPJ: " + fornecedor.getCnpj() + "\n\n";
        mensagem += "Nome: " + fornecedor.getNome() + "\n\n";

        if (!fornecedor.getFantasia().isEmpty()) {
            mensagem += "Nome Fantasia: " + fornecedor.getFantasia() + "\n\n";
        }

        mensagem += "Deseja Cadastrar Este Fornecedor?";

        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean result = cadastrarFornecedorController.cadastrar(fornecedor);

                if (result) {
                    Intent intent = new Intent();
                    intent.putExtra("fornecedor", fornecedor);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        alertaCadastro.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Fornecedor Não Foi Cadastrado");
            }
        });

        alertaCadastro.show();
    }
}
