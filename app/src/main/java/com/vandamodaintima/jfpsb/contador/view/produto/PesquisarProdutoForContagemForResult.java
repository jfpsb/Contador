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
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoForContagemForResultController;
import com.vandamodaintima.jfpsb.contador.model.Produto;

public class PesquisarProdutoForContagemForResult extends PesquisarProduto {
    private String codigo;
    private AlertDialog.Builder alertaQuantidadeProduto;

    private static final int TELA_SELECIONAR_PRODUTO = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //Código de barras usado na pesquisa inicial
        codigo = getArguments().getString("codigo");

        controller = new PesquisarProdutoForContagemForResultController(this, conexaoBanco);
        setAlertaQuantidadeProduto();

        return view;
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        controller.carregaProduto(cod_barra);
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
                int quantidade = 1;

                if (!txtQuant.isEmpty()) {
                    quantidade = Integer.parseInt(txtQuant);
                }

                if (quantidade < 1) {
                    mensagemAoUsuario("Informe Uma Quantidade Válida");
                    return;
                }

                if (codigo != null && !codigo.isEmpty())
                    controller.addCodBarraFornecedor(codigo);

                ((PesquisarProdutoForContagemForResultController)controller).atualizar();

                Intent intent = new Intent();
                intent.putExtra("produto", controller.getProduto());
                intent.putExtra("quantidade", quantidade);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_SELECIONAR_PRODUTO) {
            if (resultCode == getActivity().RESULT_OK) {
                Produto produto = (Produto) data.getSerializableExtra("produto");
                controller.carregaProduto(produto);
                int quantidade = data.getIntExtra("quantidade", 1);
                ((PesquisarProdutoForContagemForResultController)controller).cadastrar(quantidade);
            }
        }
    }
}
