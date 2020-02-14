package com.vandamodaintima.jfpsb.contador.view.produto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.PesquisarProdutoController;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarProduto extends TelaPesquisa {

    protected Spinner spinnerPesquisa;
    protected AutoCompleteTextView txtPesquisaProduto;
    protected TextView txtQuantProdutosCadastrados;

    private static final int DESCRICAO = 0;
    private static final int COD_DE_BARRA = 1;
    private static final int FORNECEDOR = 2;
    private static final int MARCA = 3;

    private static int PESQUISA = DESCRICAO;

    protected PesquisarProdutoController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        telaPesquisaView = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        listView = telaPesquisaView.findViewById(R.id.listViewProduto);
        spinnerPesquisa = telaPesquisaView.findViewById(R.id.spinnerPesquisa);
        txtPesquisaProduto = telaPesquisaView.findViewById(R.id.txtPesquisaProduto);
        txtQuantProdutosCadastrados = telaPesquisaView.findViewById(R.id.txtQuantProdutosCadastrados);

        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.auto_complete_textview_produto_items));
        txtPesquisaProduto.setAdapter(autoCompleteAdapter);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new PesquisarProdutoController(this, conexaoBanco);

        spinnerPesquisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case DESCRICAO:
                        txtPesquisaProduto.setHint("Digite a Descrição do Produto");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = DESCRICAO;
                        break;
                    case COD_DE_BARRA:
                        txtPesquisaProduto.setHint("Digite o Código de Barra");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                        PESQUISA = COD_DE_BARRA;
                        break;
                    case FORNECEDOR:
                        txtPesquisaProduto.setHint("Digite o Nome do Fornecedor");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = FORNECEDOR;
                        break;
                    case MARCA:
                        txtPesquisaProduto.setHint("Digite o Nome da Marca");
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        PESQUISA = MARCA;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtPesquisaProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                realizarPesquisa();
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_ALTERAR_DELETAR) {
            if (resultCode == Activity.RESULT_OK) {
                realizarPesquisa();
            } else {
                mensagemAoUsuario("Produto Não Foi Alterado");
            }
        }
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {
        txtQuantProdutosCadastrados.setText(String.valueOf(quantidade));
    }

    @Override
    public void realizarPesquisa() {
        String termo = txtPesquisaProduto.getText().toString();

        switch (PESQUISA) {
            case DESCRICAO:
                controller.pesquisarPorDescricao(termo);
                break;
            case COD_DE_BARRA:
                controller.pesquisarPorCodBarra(termo);
                break;
            case FORNECEDOR:
                controller.pesquisarPorFornecedor(termo);
                break;
            case MARCA:
                controller.pesquisarPorMarca(termo);
                break;
        }
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        Intent alterarProduto = new Intent(getContext(), AlterarDeletarProduto.class);
        alterarProduto.putExtra("produto", cod_barra);

        startActivityForResult(alterarProduto, TELA_ALTERAR_DELETAR);
    }
}