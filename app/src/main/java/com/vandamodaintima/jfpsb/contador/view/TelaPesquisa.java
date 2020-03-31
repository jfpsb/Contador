package com.vandamodaintima.jfpsb.contador.view;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public abstract class TelaPesquisa extends Fragment implements PesquisarView {
    protected static final int TELA_ALTERAR_DELETAR = 1;

    protected ListView listView;
    protected View telaPesquisaView;
    protected ConexaoBanco conexaoBanco;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cliqueEmItemLista(adapterView, i);
            }
        });

        return telaPesquisaView;
    }

    @Override
    public void onDestroy() {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter instanceof CursorAdapter) {
            CursorAdapter cursorAdapter = (CursorAdapter) listAdapter;
            Cursor cursor = cursorAdapter.getCursor();
            if (cursor != null)
                cursor.close();
        }

        conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
