package com.vandamodaintima.jfpsb.contador;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

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

        String loja = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));

        data = TrataDisplayData.getDataFormatoDisplay(data);

        labelLojaNome.setText(loja);
        labelDataInicial.setText(data);
    }
}
