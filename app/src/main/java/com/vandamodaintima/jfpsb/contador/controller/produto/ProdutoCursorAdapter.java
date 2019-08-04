//package com.vandamodaintima.jfpsb.contador.controller.produtoModel;
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
//import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
//
//import java.util.List;
//
//public class ProdutoCursorAdapter extends ArrayAdapter<ProdutoModel> {
//    private int resourceLayout;
//
//    public ProdutoCursorAdapter(Context context, int resource, List<ProdutoModel> objects) {
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
//        ProdutoModel p = getItem(position);
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

package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

public class ProdutoCursorAdapter extends CursorAdapter {
    public ProdutoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_produto, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelCodBarra = view.findViewById(R.id.text1);
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