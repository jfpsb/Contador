package com.vandamodaintima.jfpsb.contador.view;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public class TelaPesquisa extends Fragment implements PesquisarView {
    protected ListView listView;
    protected View view;
    protected SQLiteDatabase sqLiteDatabase;

    @Override
    public void onDestroy() {
        sqLiteDatabase.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void populaLista(ArrayAdapter adapter) {
        listView.setAdapter(null);
        listView.setAdapter(adapter);
    }
}
