package com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor;


import android.app.Activity;
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
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFornecedor extends TelaPesquisa {

    private static FornecedorManager fornecedorManager;
    protected ListView listView;
    private FornecedorCursorAdapter fornecedorCursorAdapter;
    private EditText txtPesquisaFornecedor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_pesquisar_fornecedor);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(((TabLayoutActivityBase) getActivity()).getConn());
    }

    @Override
    protected void setViews() {
        setTxtPesquisaFornecedor();
        setListView();
    }

    private void setTxtPesquisaFornecedor() {
        txtPesquisaFornecedor = viewInflate.findViewById(R.id.txtPesquisaFornecedor);

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

    @Override
    protected void setListView() {
        listView = viewInflate.findViewById(R.id.listViewLoja);

        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = fornecedorManager.listarCursor();
            fornecedorCursorAdapter = new FornecedorCursorAdapter(viewInflate.getContext(), cursorPesquisa);
            listView.setAdapter(fornecedorCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.setListView();
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                fornecedor.setFantasia(cursor.getString(cursor.getColumnIndexOrThrow("fantasia")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("fornecedor", fornecedor);

                Intent alterarFornecedor = new Intent(viewInflate.getContext(), AlterarDeletarFornecedor.class);

                alterarFornecedor.putExtras(bundle);

                startActivityForResult(alterarFornecedor, TELA_ALTERAR_DELETAR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if (resultCode == Activity.RESULT_OK) {
                    populaListView(txtPesquisaFornecedor.getText().toString());
                } else {
                    Toast.makeText(viewInflate.getContext(), "O Fornecedor Não Foi Alterado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Popula a lista novamente
     */
    public void populaListView() {
        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = fornecedorManager.listarCursor();
            fornecedorCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void populaListView(String termo) {
        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = fornecedorManager.listarCursorPorNomeCnpjFantasia(termo);
            fornecedorCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}