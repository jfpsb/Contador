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

public class FornecedorCursorAdapter extends CursorAdapter {
    public FornecedorCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_id_nome, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelCnpj = view.findViewById(R.id.labelId);
        TextView labelNome = view.findViewById(R.id.labelNome);

        String cnpj = cursor.getString(cursor.getColumnIndexOrThrow("cnpj"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));

        labelCnpj.setText(cnpj);
        labelNome.setText(nome);
    }
}
