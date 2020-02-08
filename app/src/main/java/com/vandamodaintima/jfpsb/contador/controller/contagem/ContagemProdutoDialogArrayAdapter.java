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
import com.vandamodaintima.jfpsb.contador.model.Produto;

import java.util.List;

public class ContagemProdutoDialogArrayAdapter extends ArrayAdapter<Produto> {
    private int resourceLayout;
    private Context mContext;
    private int index = 0;

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
            final CheckedTextView txtProduto = v.findViewById(R.id.txtProdutoDialog);
            // Marca o item que tem posição igual à index
            txtProduto.setChecked(position == index);
            // Guarda na propriedade tag a posição de cada item na lista
            txtProduto.setTag(position);

            //Lida com a seleção do radiobutton pq por algum motivo parou de funcionar sozinho então fiz manualmente.
            //Se sobreescrever o onClickListener não funciona então tem que usar o setOnTouchListener
            txtProduto.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Ao tocar na opção, salva em index a posição do item selecionado
                    index = (int) view.getTag();
                    notifyDataSetChanged();
                    return false;
                }
            });

            String s = p.getCod_barra() + " - " + p.getDescricao();
            txtProduto.setText(s);
        }

        return v;
    }

    @Override
    /**
     * Reseta o index para zero para retornar marcação em lista para primeiro item
     */
    public void clear() {
        index = 0;
        super.clear();
    }
}