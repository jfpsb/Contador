package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Loja;

import java.util.List;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class LojaArrayAdapter extends ArrayAdapter<Loja> {
    private int resourceLayout;
    private Context mContext;

    public LojaArrayAdapter(Context context, int resource, List<Loja> objects) {
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

        Loja p = getItem(position);

        if (p != null) {
            TextView tt1 = v.findViewById(R.id.labelId);
            TextView tt2 = v.findViewById(R.id.labelNome);

            if (tt1 != null) {
                tt1.setText(p.getCnpj());
            }

            if (tt2 != null) {
                tt2.setText(p.getNome());
            }
        }

        return v;
    }
}