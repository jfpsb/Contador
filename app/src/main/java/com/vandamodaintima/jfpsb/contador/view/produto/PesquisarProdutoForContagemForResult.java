package com.vandamodaintima.jfpsb.contador.view.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

public class PesquisarProdutoForContagemForResult extends PesquisarProduto {
    private String codigo;
    private ProdutoModel produtoModel;
    private AlertDialog.Builder alertaQuantidadeProduto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Código de barras usado na pesquisa inicial
        codigo = getArguments().getString("codigo");

        setAlertaQuantidadeProduto();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        produtoModel = pesquisarProdutoController.retornaProdutoEscolhidoListView(cod_barra);

        alertaQuantidadeProduto.show();
    }

    private void setAlertaQuantidadeProduto() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = view.findViewById(R.id.txtQuantidade);

        alertaQuantidadeProduto = new AlertDialog.Builder(getActivity());
        alertaQuantidadeProduto.setView(view);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txtQuant = txtQuantidade.getText().toString();

                if (!txtQuant.isEmpty()) {
                    int quantidade = Integer.parseInt(txtQuant);

                    if (quantidade < 1) {
                        mensagemAoUsuario("Informe Uma Quantidade Válida");
                        return;
                    }

                    produtoModel.getCod_barra_fornecedor().add(codigo);
                    pesquisarProdutoController.atualizar(produtoModel);

                    Intent intent = new Intent();
                    intent.putExtra("produto", produtoModel.getCod_barra());
                    intent.putExtra("quantidade", quantidade);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada");
            }
        });
    }
}
