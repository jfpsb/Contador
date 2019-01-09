package com.vandamodaintima.jfpsb.contador.controller.contagem;

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

public class ContagemProdutoDialogArrayAdapter extends ArrayAdapter<Produto> {
    private int resourceLayout;
    private Context mContext;

    public ContagemProdutoDialogArrayAdapter(Context context, int resource, List<Produto> objects) {
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
            TextView text1 = v.findViewById(R.id.txtProdutoDialog);

            if (text1 != null) {
                text1.setText(p.getCod_barra() + " - " + p.getDescricao());
            }
        }

        return v;
    }
}