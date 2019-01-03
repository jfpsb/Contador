package com.vandamodaintima.jfpsb.contador.controller.produto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.List;

public class ProdutoAdapter extends ArrayAdapter<Produto> {
    private int resourceLayout;
    private Context mContext;

    public ProdutoAdapter(Context context, int resource, List<Produto> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Produto p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.labelCodBarra);
            TextView tt2 = (TextView) v.findViewById(R.id.labelDescricao);
            TextView tt3 = (TextView) v.findViewById(R.id.labelPreco);

            if (tt1 != null) {
                tt1.setText(p.getCod_barra());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescricao());
            }

            if (tt3 != null) {
                tt3.setText(p.getPreco().toString());
            }
        }

        return v;
    }
}