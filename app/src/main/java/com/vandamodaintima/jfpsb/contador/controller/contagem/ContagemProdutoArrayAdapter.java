package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;

import java.util.List;

/**
 * Created by jfpsb on 09/02/2018.
 */

public class ContagemProdutoArrayAdapter extends ArrayAdapter<ContagemProduto> {
    private int resourceLayout;
    private Context mContext;

    public ContagemProdutoArrayAdapter(Context context, int resource, List<ContagemProduto> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            view = vi.inflate(resourceLayout, null);
        }

        ContagemProduto cp = getItem(position);

        if (cp != null) {
            TextView txtCodProduto = view.findViewById(R.id.txtCodigoProduto);
            TextView txtDescricaoProduto = view.findViewById(R.id.txtDescricaoProduto);
            TextView txtCodBarra = view.findViewById(R.id.txtCodBarra);
            TextView txtDescricaoGrade = view.findViewById(R.id.txtDescricaoGrade);
            TextView txtQuantidade = view.findViewById(R.id.lblQuantidade);

            txtCodProduto.setText(cp.getProdutoGrade().getProduto().getCodBarra());
            txtDescricaoProduto.setText(cp.getProdutoGrade().getProduto().getDescricao());
            txtQuantidade.setText(String.valueOf(cp.getQuant()));
            txtCodBarra.setText(cp.getProdutoGrade().getCodBarra());
            txtDescricaoGrade.setText(cp.getProdutoGrade().getGradesToShortString());
        }

        return view;
    }
}