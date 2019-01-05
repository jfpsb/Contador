package com.vandamodaintima.jfpsb.contador.controller.produtocontagem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoContagem;

import java.util.List;

public class ProdutoContagemAdapter extends ArrayAdapter<ProdutoContagem> {
    private int resourceLayout;
    private Context mContext;

    public ProdutoContagemAdapter(Context context, int resource, List<ProdutoContagem> objects) {
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

        ProdutoContagem pc = getItem(position);

        if (pc != null) {
            TextView labelCodBarra = v.findViewById(R.id.text1);
            TextView labelDescricao = v.findViewById(R.id.labelDescricao);
            TextView labelQuant = v.findViewById(R.id.labelQuant);

            labelCodBarra.setText(pc.getProduto().getCod_barra());
            labelDescricao.setText(pc.getProduto().getDescricao());
            labelQuant.setText(String.valueOf(pc.getQuant()));
        }

        return v;
    }
}
