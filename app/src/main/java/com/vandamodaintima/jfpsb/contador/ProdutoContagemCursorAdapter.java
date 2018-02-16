package com.vandamodaintima.jfpsb.contador;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jfpsb on 16/02/2018.
 */

public class ProdutoContagemCursorAdapter extends CursorAdapter {
    public ProdutoContagemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_produto_contagem, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelCodBarra = view.findViewById(R.id.labelCodBarra);
        TextView labelDescricao = view.findViewById(R.id.labelDescricao);
        TextView labelQuant = view.findViewById(R.id.labelQuant);

        int cod_barra = cursor.getInt(cursor.getColumnIndexOrThrow("produto"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        int quant = cursor.getInt(cursor.getColumnIndexOrThrow("quant"));

        labelCodBarra.setText(String.valueOf(cod_barra));
        labelDescricao.setText(descricao);
        labelQuant.setText(String.valueOf(quant));
    }
}
