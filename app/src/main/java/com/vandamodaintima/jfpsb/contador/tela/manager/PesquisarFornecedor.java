package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.FornecedorCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFornecedor extends FragmentBase {

    private static DAOFornecedor daoFornecedor;
    private ListView listView;
    private static FornecedorCursorAdapter fornecedorCursorAdapter;
    private EditText txtPesquisaFornecedor;

    public PesquisarFornecedor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_fornecedor, container, false);

        setDAOs();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        txtPesquisaFornecedor = viewInflate.findViewById(R.id.txtPesquisaFornecedor);

        setTxtPesquisaFornecedor();
        setListView();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDAOs() {
        daoFornecedor = new DAOFornecedor(((ActivityBase) getActivity()).getConn().conexao());
    }

    private void setTxtPesquisaFornecedor() {
        txtPesquisaFornecedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populaListView(txtPesquisaFornecedor.getText().toString());
            }
        });
    }

    private void setListView() {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoFornecedor.selectFornecedores();

            fornecedorCursorAdapter = new FornecedorCursorAdapter(viewInflate.getContext(), cursorLista);

            listView.setAdapter(fornecedorCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("fornecedor", fornecedor);

                Intent alterarFornecedor = new Intent(viewInflate.getContext(), AlterarDeletarFornecedor.class);

                alterarFornecedor.putExtras(bundle);

                startActivity(alterarFornecedor);
            }
        });
    }

    /**
     * Popula a lista novamente
     */

    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Toast.makeText(viewInflate.getContext(), "Pesquisando todos os forncedores", Toast.LENGTH_SHORT).show();

        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoFornecedor.selectFornecedores();
            fornecedorCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void populaListView(String nome) {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoFornecedor.selectFornecedores(nome);
            fornecedorCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}