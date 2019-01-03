package com.vandamodaintima.jfpsb.contador.view.fornecedor;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.PesquisarFornecedorController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFornecedor extends Fragment implements PesquisarView {

    protected ListView listView;
    private EditText txtPesquisaFornecedor;

    private PesquisarFornecedorController pesquisarFornecedorController;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_fornecedor, container, false);

        sqLiteDatabase = new ConexaoBanco(getContext()).conexao();
        pesquisarFornecedorController = new PesquisarFornecedorController(this, sqLiteDatabase, getContext());

        txtPesquisaFornecedor = viewInflate.findViewById(R.id.txtPesquisaFornecedor);
        listView = viewInflate.findViewById(R.id.listViewLoja);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fornecedor fornecedor = (Fornecedor) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(getContext(), AlterarDeletarFornecedor.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("fornecedor", fornecedor);

                intent.putExtras(bundle);

                startActivity(intent);
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
                pesquisarFornecedorController.pesquisa(txtPesquisaFornecedor.getText().toString());
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