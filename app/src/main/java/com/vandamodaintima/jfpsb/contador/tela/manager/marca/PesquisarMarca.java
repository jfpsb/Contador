package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

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
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;

import org.apache.poi.ss.formula.eval.NotImplementedException;

public class PesquisarMarca extends TelaPesquisa {
    private EditText txtNome;
    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;

    private MarcaManager marcaManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_marca, container, false);

        listView = viewInflate.findViewById(R.id.listViewMarca);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        setManagers();
        setListView();
        setTxtNome();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);

                Marca marca = new Marca();

                marca.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                marca.setNome(c.getString(c.getColumnIndexOrThrow("nome")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("marca", marca);

                Intent intent = new Intent(viewInflate.getContext(), AlterarDeletarMarca.class);
                intent.putExtras(bundle);

                startActivityForResult(intent, TELA_ALTERAR_DELETAR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if(resultCode == Activity.RESULT_OK) {
                    populaListView();
                }
                else {
                    Toast.makeText(viewInflate.getContext(), "A Marca Não Foi Alterada", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setTxtNome() {
        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                populaListView(txtNome.getText().toString());
            }
        });
    }

    @Override
    protected void setManagers() {
        marcaManager = new MarcaManager(((ActivityBase) getActivity()).getConn());
    }

    @Override
    protected void setListView() {
        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = marcaManager.listarCursor();
            simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), R.layout.item_lista_marca, cursorPesquisa, new String[]{"nome"}, new int[]{R.id.labelMarcaNome}, 0);
            listView.setAdapter(simpleCursorAdapter);

            setListOnItemClickListener();
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Popula a lista novamente
     */
    public void populaListView() {
        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = marcaManager.listarCursor();

            if(cursorPesquisa.getCount() == 0)
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();

            simpleCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void populaListView(String nome) {
        if (cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = marcaManager.listarCursorPorNome(nome);

            if(cursorPesquisa.getCount() == 0)
                Toast.makeText(viewInflate.getContext(), "A Pesquisa Não Retornou Dados", Toast.LENGTH_SHORT).show();

            simpleCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
