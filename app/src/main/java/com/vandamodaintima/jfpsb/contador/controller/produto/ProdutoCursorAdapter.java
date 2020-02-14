package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.Cursor;
import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class ProdutoCursorAdapter extends CursorAdapter {
    ProdutoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_produto, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelCodBarra = view.findViewById(R.id.labelCodBarra);
        TextView labelDescricao = view.findViewById(R.id.labelDescricao);
        TextView labelPreco = view.findViewById(R.id.labelPreco);

        String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        Double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));

        labelCodBarra.setText(id);
        labelDescricao.setText(descricao);
        labelPreco.setText(String.valueOf(preco));
    }
}