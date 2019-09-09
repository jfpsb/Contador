package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.PesquisarFornecedorController;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarFornecedor extends TelaPesquisa {
    private EditText txtPesquisaFornecedor;

    protected PesquisarFornecedorController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_fornecedor, container, false);

        txtPesquisaFornecedor = view.findViewById(R.id.txtPesquisaFornecedor);
        listView = view.findViewById(R.id.listViewLoja);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new PesquisarFornecedorController(this, conexaoBanco);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cliqueEmItemLista(adapterView, i);
            }
        });

        txtPesquisaFornecedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                controller.pesquisa(txtPesquisaFornecedor.getText().toString());
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    realizarPesquisa();
                } else {
                    mensagemAoUsuario("O Fornecedor Não Foi Alterado");
                }
                break;
        }
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {

    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        Intent intent = new Intent(getContext(), AlterarDeletarFornecedor.class);
        intent.putExtra("fornecedor", cnpj);
        startActivityForResult(intent, TELA_ALTERAR_DELETAR);
    }

    @Override
    public void realizarPesquisa() {
        controller.pesquisa(txtPesquisaFornecedor.getText().toString());
    }
}