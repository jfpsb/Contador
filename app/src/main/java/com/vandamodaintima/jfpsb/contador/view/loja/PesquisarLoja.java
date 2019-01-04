package com.vandamodaintima.jfpsb.contador.view.loja;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.loja.PesquisarLojaController;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarLoja extends TelaPesquisa {

    private ListView listView;
    private EditText txtNome;
    private PesquisarLojaController pesquisarLojaController;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_loja, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarLojaController = new PesquisarLojaController(this, sqLiteDatabase, getContext());

        txtNome = viewInflate.findViewById(R.id.txtNome);
        listView = viewInflate.findViewById(R.id.listViewLoja);

        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pesquisarLojaController.pesquisar(txtNome.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Loja loja = (Loja) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), AlterarDeletarLoja.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("loja", loja);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        return viewInflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populaLista(ArrayAdapter adapter) {
        listView.setAdapter(null);
        listView.setAdapter(adapter);
    }
}
