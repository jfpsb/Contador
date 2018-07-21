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

public class ContagemCursorAdapter extends CursorAdapter {
    public ContagemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_contagem, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelLojaNome = view.findViewById(R.id.labelLojaNome);
        TextView labelDataInicial = view.findViewById(R.id.labelDataInicial);

        String lojaNome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        String datainicial = cursor.getString(cursor.getColumnIndexOrThrow("datainicio"));

        labelLojaNome.setText(String.valueOf(lojaNome));
        labelDataInicial.setText(datainicial);
    }
}
