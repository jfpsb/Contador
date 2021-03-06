package com.vandamodaintima.jfpsb.contador.view.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.R;

public class CadastrarProdutoForContagemForResult extends CadastrarProduto {
    private AlertDialog.Builder alertaQuantidadeProduto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Se for informado o código de barras ao abrir a tela
        String codigo = getArguments().getString("codigo");
        if (codigo != null) {
            txtCodBarra.setText(codigo);
            controller.getProduto().setCod_barra(codigo);
        }

        setAlertaQuantidadeProduto();

        return view;
    }

    @Override
    public void aposCadastro(Object... args) {
        alertaQuantidadeProduto.show();
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

            if (!txtQuant.isEmpty()) {
                int quantidade = Integer.parseInt(txtQuant);

                if (quantidade < 1) {
                    mensagemAoUsuario("Informe Uma Quantidade Válida");
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("produto", controller.getProduto());
                intent.putExtra("quantidade", quantidade);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", (dialogInterface, i) -> mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada"));
    }
}
