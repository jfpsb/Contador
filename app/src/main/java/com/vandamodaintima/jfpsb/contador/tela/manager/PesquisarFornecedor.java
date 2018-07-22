package com.vandamodaintima.jfpsb.contador.tela.manager;


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
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFornecedor extends FragmentBase {

    private static FornecedorManager fornecedorManager;
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

        setManagers();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        txtPesquisaFornecedor = viewInflate.findViewById(R.id.txtPesquisaFornecedor);

        setTxtPesquisaFornecedor();
        setListView();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(((ActivityBase) getActivity()).getConn());
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
            cursorLista = fornecedorManager.listarCursor();

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
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));

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
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = fornecedorManager.listarCursor();
            fornecedorCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void populaListView(String termo) {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = fornecedorManager.listarPorNomeOuCnpj(termo);
            fornecedorCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}