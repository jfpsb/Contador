package com.vandamodaintima.jfpsb.contador.tela.manager;


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

import com.vandamodaintima.jfpsb.contador.LojaCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarLoja extends TelaPesquisa {

    private static LojaManager lojaManager;
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

        setManagers();

        listView = viewInflate.findViewById(R.id.listViewLoja);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        setTxtNome();
        setListView();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(((ActivityBase) getActivity()).getConn());
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

    @Override
    protected void setListView() {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = lojaManager.listarCursor();
            lojaCursorAdapter = new LojaCursorAdapter(viewInflate.getContext(), cursorPesquisa);
            listView.setAdapter(lojaCursorAdapter);
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

                Loja loja = new Loja();

                loja.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                loja.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("loja", loja);

                Intent alterarLoja = new Intent(viewInflate.getContext(), AlterarDeletarLoja.class);

                alterarLoja.putExtras(bundle);

                startActivityForResult(alterarLoja, TELA_ALTERAR_DELETAR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if(resultCode == Activity.RESULT_OK) {
                    populaListView(txtNome.getText().toString());
                }
                else {
                    Toast.makeText(viewInflate.getContext(), "A Loja Não Foi Alterada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * Popula a lista novamente
     */

    public void populaListView() {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = lojaManager.listarCursor();

            if(cursorPesquisa.getCount() == 0)
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();

            lojaCursorAdapter.changeCursor(cursorPesquisa);
        }catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void populaListView(String nome) {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = lojaManager.listarPorNome(nome);

            if(cursorPesquisa.getCount() == 0)
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();

            lojaCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
