package com.vandamodaintima.jfpsb.contador.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class TelaPesquisa extends Fragment implements PesquisarView {
    protected ListView listView;
    protected View view;
    protected SQLiteDatabase sqLiteDatabase;

    @Override
    public void onDestroy() {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter instanceof CursorAdapter) {
            CursorAdapter cursorAdapter = (CursorAdapter) listAdapter;
            Cursor cursor = cursorAdapter.getCursor();
            if (cursor != null)
                cursor.close();
        }

        if(sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();

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

    @Override
    public void setTextoQuantidadeBusca(int quantidade) {
        Log.i("Contador", "Sobreescreva na fragment se precisar informar a quantidade de itens retornados na pesquisa");
    }

    @Override
    public void realizarPesquisa() {
        try {
            throw new Exception("Sobreescreva na fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cliqueEmItemLista(AdapterView<?> adapterView, int i) {
        try {
            throw new Exception("Sobreescreva na fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
