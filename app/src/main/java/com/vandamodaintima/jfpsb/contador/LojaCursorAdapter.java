package com.vandamodaintima.jfpsb.contador;

import android.content.Context;
import android.database.Cursor;

import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class LojaCursorAdapter extends CursorAdapter {
    public LojaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_loja, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelIdLoja = view.findViewById(R.id.labelIdLoja);
        TextView labelNomeLoja = view.findViewById(R.id.labelNomeLoja);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));

        labelIdLoja.setText(String.valueOf(id));
        labelNomeLoja.setText(nome);
    }
}
