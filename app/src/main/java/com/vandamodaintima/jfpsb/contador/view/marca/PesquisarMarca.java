package com.vandamodaintima.jfpsb.contador.view.marca;

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

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.marca.PesquisarMarcaController;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarMarca extends TelaPesquisa {
    private EditText txtNome;

    protected PesquisarMarcaController pesquisarMarcaController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_marca, container, false);

        txtNome = view.findViewById(R.id.txtNome);
        listView = view.findViewById(R.id.listViewMarca);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarMarcaController = new PesquisarMarcaController(this, sqLiteDatabase, getContext());

        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                realizarPesquisa();
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
                    mensagemAoUsuario("A Marca Não Foi Alterada");
                }
                break;
        }
    }

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {

    }

    @Override
    public void realizarPesquisa() {
        pesquisarMarcaController.pesquisar(txtNome.getText().toString());
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

        Marca marca = new Marca();
        marca.setId(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
        marca.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

        Bundle bundle = new Bundle();
        bundle.putSerializable("marca", marca);

        Intent intent = new Intent(getContext(), AlterarDeletarMarca.class);
        intent.putExtras(bundle);

        startActivityForResult(intent, TELA_ALTERAR_DELETAR);
    }
}