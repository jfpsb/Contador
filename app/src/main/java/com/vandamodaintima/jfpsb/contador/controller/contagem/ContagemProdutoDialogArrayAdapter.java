package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

import java.util.List;

public class ContagemProdutoDialogArrayAdapter extends ArrayAdapter<ProdutoModel> {
    private int resourceLayout;
    private Context mContext;
    private int index = 0;

    public ContagemProdutoDialogArrayAdapter(Context context, int resource, List<ProdutoModel> objects) {
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

        ProdutoModel p = getItem(position);

        if (p != null) {
            final CheckedTextView text1 = v.findViewById(R.id.txtProdutoDialog);
            text1.setChecked(position == index);
            text1.setTag(position);

            //Lida com a seleção do radiobutton pq por algum motivo parou de funcionar sozinho
            //então fiz manualmente.
            //Se sobreescrever o onClickListener não funciona então tem que usar esse ai
            text1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    index = (int) view.getTag();
                    notifyDataSetChanged();
                    return false;
                }
            });

            text1.setText(p.getCod_barra() + " - " + p.getDescricao());
        }

        return v;
    }
}