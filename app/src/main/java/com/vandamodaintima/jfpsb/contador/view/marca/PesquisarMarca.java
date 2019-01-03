package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

public class PesquisarMarca extends TelaPesquisa {
    private EditText txtNome;
    protected ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pesquisar_marca, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();

        txtNome = view.findViewById(R.id.txtNome);
        listView = view.findViewById(R.id.listViewMarca);

        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                populaListView(txtNome.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);

                Marca marca = new Marca();
                marca.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
                marca.setNome(c.getString(c.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();
                bundle.putSerializable("marca", marca);

                Intent intent = new Intent(getContext(), AlterarDeletarMarca.class);
                intent.putExtras(bundle);

                startActivityForResult(intent, TELA_ALTERAR_DELETAR);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    //populaListView();
                } else {
                    Toast.makeText(getContext(), "A Marca Não Foi Alterada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
