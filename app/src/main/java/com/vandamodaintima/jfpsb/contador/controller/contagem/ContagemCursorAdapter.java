//package com.vandamodaintima.jfpsb.contador.controller.produto;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.vandamodaintima.jfpsb.contador.R;
//import com.vandamodaintima.jfpsb.contador.model.Produto;
//
//import java.util.List;
//
//public class ProdutoCursorAdapter extends ArrayAdapter<Produto> {
//    private int resourceLayout;
//
//    public ProdutoCursorAdapter(Context context, int resource, List<Produto> objects) {
//        super(context, resource, objects);
//        this.resourceLayout = resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View v = convertView;
//
//        if (v == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(getContext());
//            v = vi.inflate(resourceLayout, null);
//        }
//
//        Produto p = getItem(position);
//
//        if (p != null) {
//            TextView tt1 = v.findViewById(R.id.labelCodBarra);
//            TextView tt2 = v.findViewById(R.id.labelDescricao);
//            TextView tt3 = v.findViewById(R.id.labelPreco);
//
//            tt1.setText(p.getCod_barra());
//            tt2.setText(p.getDescricao());
//            tt3.setText(p.getPreco().toString());
//        }
//
//        return v;
//    }
//}

package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;

import java.util.Date;

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

        Date data2 = Contagem.getData(data);

        labelLojaNome.setText(loja);
        labelDataInicial.setText(Contagem.getDataString(data2));
    }
}