package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;

public class CadastrarFornecedorForResult extends CadastrarFornecedor {
    private FornecedorModel fornecedor;
    @Override
    public void setAlertaCadastro(final FornecedorModel fornecedor) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar FornecedorModel");

        String mensagem = "Confirme os Dados do FornecedorModel Encontrado Com CNPJ: " + fornecedor.getCnpj() + "\n\n";
        mensagem += "Nome: " + fornecedor.getNome() + "\n\n";

        if (!fornecedor.getFantasia().isEmpty()) {
            mensagem += "Nome Fantasia: " + fornecedor.getFantasia() + "\n\n";
        }

        mensagem += "Deseja Cadastrar Este FornecedorModel?";

        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CadastrarFornecedorForResult.this.fornecedor = fornecedor;
                cadastrarFornecedorController.cadastrar(fornecedor);
            }
        });

        alertaCadastro.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("FornecedorModel Não Foi Cadastrado");
            }
        });

        alertaCadastro.show();
    }

    @Override
    public void aposCadastro(Object... args) {
        Intent intent = new Intent();
        intent.putExtra("fornecedor", fornecedor);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
