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

import com.vandamodaintima.jfpsb.contador.LojaCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.security.spec.ECField;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarLoja extends FragmentBase {

    private static DAOLoja daoLoja;
    private ListView listView;
    private static LojaCursorAdapter lojaCursorAdapter;
    private EditText txtNome;

    public PesquisarLoja() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_loja, container, false);

        setDAOs();

        listView = viewInflate.findViewById(R.id.listViewLoja);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        setTxtNome();
        setListView();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDAOs() {
        daoLoja = new DAOLoja(((ActivityBase) getActivity()).getConn().conexao());
    }

    private void setTxtNome() {
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
    }

    private void setListView() {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoLoja.selectLojas();

            lojaCursorAdapter = new LojaCursorAdapter(viewInflate.getContext(), cursorLista);

            listView.setAdapter(lojaCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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
    }

    /**
     * Popula a lista novamente
     */

    public static void populaListView() {
        // Switch to new cursor and update contents of ListView
        Toast.makeText(viewInflate.getContext(), "Pesquisando todas as lojas", Toast.LENGTH_SHORT).show();

        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoLoja.selectLojas();
            lojaCursorAdapter.changeCursor(cursorLista);
        }catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void populaListView(String nome) {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = daoLoja.selectLojas(nome);
            lojaCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
