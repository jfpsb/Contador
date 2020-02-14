package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.database.Cursor;
import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class LojaCursorAdapter extends CursorAdapter {
    LojaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_id_nome, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelIdLoja = view.findViewById(R.id.labelId);
        TextView labelNomeLoja = view.findViewById(R.id.labelNome);

        String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));

        labelIdLoja.setText(cnpj);
        labelNomeLoja.setText(nome);
    }
}