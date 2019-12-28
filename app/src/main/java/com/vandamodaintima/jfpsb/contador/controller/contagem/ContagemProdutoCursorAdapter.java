package com.vandamodaintima.jfpsb.contador.controller.contagem;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class ContagemProdutoCursorAdapter extends CursorAdapter {

    public ContagemProdutoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_produto_contagem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView lblCodBarra = view.findViewById(R.id.lblCodBarra);
        TextView lblDescricao = view.findViewById(R.id.lblDescricao);
        TextView lblQuantidade = view.findViewById(R.id.lblQuantidade);

        String cod_barra = cursor.getString(cursor.getColumnIndexOrThrow("produto"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quant"));

        lblCodBarra.setText(cod_barra);
        lblDescricao.setText(descricao);
        lblQuantidade.setText(String.valueOf(quantidade));
    }
}
