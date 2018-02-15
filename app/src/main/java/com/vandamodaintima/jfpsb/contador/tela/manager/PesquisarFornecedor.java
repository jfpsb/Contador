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

import com.vandamodaintima.jfpsb.contador.FornecedorCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFornecedor extends Fragment {
    private ConexaoBanco conn;
    private static DAOFornecedor daoFornecedor;
    private ListView listView;
    private static FornecedorCursorAdapter fornecedorCursorAdapter;
    private EditText txtPesquisaFornecedor;

    public PesquisarFornecedor() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_fornecedor, container, false);

        daoFornecedor = new DAOFornecedor(conn.conexao());

        Cursor cursor = daoFornecedor.selectFornecedores();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        fornecedorCursorAdapter = new FornecedorCursorAdapter(viewInflate.getContext(), cursor);

        listView.setAdapter(fornecedorCursorAdapter);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("fornecedor", fornecedor);

                Intent alterarFornecedor = new Intent(viewInflate.getContext(), AlterarDeletarFornecedor.class);

                alterarFornecedor.putExtras(bundle);

                startActivity(alterarFornecedor);
            }
        });

        return viewInflate;
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoFornecedor.selectFornecedores();
        fornecedorCursorAdapter.changeCursor(cursor);
    }

    public static void populaListView(String nome) {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoFornecedor.selectFornecedores(nome);
        fornecedorCursorAdapter.changeCursor(cursor);
    }

}