package com.vandamodaintima.jfpsb.contador.view.loja;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.vandamodaintima.jfpsb.contador.controller.loja.PesquisarLojaController;
import com.vandamodaintima.jfpsb.contador.model.LojaModel;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarLoja extends TelaPesquisa {

    private EditText txtNome;
    private PesquisarLojaController pesquisarLojaController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_loja, container, false);

        txtNome = view.findViewById(R.id.txtNome);
        listView = view.findViewById(R.id.listViewLoja);

        conexaoBanco = new ConexaoBanco(getContext());
        pesquisarLojaController = new PesquisarLojaController(this, conexaoBanco, getContext());

        txtNome.addTextChangedListener(new TextWatcher() {
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
                mensagemAoUsuario("A Loja Não Foi Alterada");
            }
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
    public void realizarPesquisa() {
        pesquisarLojaController.pesquisar(txtNome.getText().toString());
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

        LojaModel loja = pesquisarLojaController.retornaLojaEscolhidaListView(cnpj);

        Intent intent = new Intent(getContext(), AlterarDeletarLoja.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("loja", loja);

        intent.putExtras(bundle);

        startActivityForResult(intent, TELA_ALTERAR_DELETAR);
    }
}
