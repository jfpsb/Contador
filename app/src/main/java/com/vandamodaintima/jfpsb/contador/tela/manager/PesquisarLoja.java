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

import com.vandamodaintima.jfpsb.contador.LojaCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarLoja extends Fragment {
    private ConexaoBanco conn;
    private static DAOLoja daoLoja;
    private ListView listView;
    private static LojaCursorAdapter lojaCursorAdapter;
    private EditText txtNome;

    public PesquisarLoja() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_pesquisar_loja, container, false);

        daoLoja = new DAOLoja(conn.conexao());

        Cursor cursor = daoLoja.selectLojas();

        listView = viewInflate.findViewById(R.id.listViewLoja);

        lojaCursorAdapter = new LojaCursorAdapter(viewInflate.getContext(), cursor);

        listView.setAdapter(lojaCursorAdapter);

        txtNome = viewInflate.findViewById(R.id.txtNome);

        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populaListView(txtNome.getText().toString());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                Loja loja = new Loja();

                loja.setIdloja(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("loja", loja);

                Intent alterarLoja = new Intent(viewInflate.getContext(), AlterarDeletarLoja.class);

                alterarLoja.putExtras(bundle);

                startActivity(alterarLoja);
            }
        });

        return viewInflate;
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoLoja.selectLojas();
        lojaCursorAdapter.changeCursor(cursor);
    }

    public static void populaListView(String nome) {
        // Switch to new cursor and update contents of ListView
        Cursor cursor = daoLoja.selectLojas(nome);
        lojaCursorAdapter.changeCursor(cursor);
    }

}
