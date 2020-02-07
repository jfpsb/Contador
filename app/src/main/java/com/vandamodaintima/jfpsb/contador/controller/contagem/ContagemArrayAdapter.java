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
import com.vandamodaintima.jfpsb.contador.model.Contagem;

import java.util.List;

public class ContagemArrayAdapter extends ArrayAdapter<Contagem> {
    private int resourceLayout;
    private Context mContext;

    public ContagemArrayAdapter(Context context, int resource, List<Contagem> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Contagem c = getItem(position);

        if (c != null) {
            TextView labelLojaNome = v.findViewById(R.id.labelLojaNome);
            TextView labelDataInicial = v.findViewById(R.id.labelDataCriada);
            TextView labelTipoContagem = v.findViewById(R.id.labelTipoContagem);

            labelLojaNome.setText(c.getLoja().getNome());
            labelDataInicial.setText(c.getFullDataString());
            labelTipoContagem.setText(c.getTipoContagem().getNome());
        }

        return v;
    }
}
