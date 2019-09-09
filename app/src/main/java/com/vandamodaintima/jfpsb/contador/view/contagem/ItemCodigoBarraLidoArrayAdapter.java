package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

import java.util.List;

public class ItemCodigoBarraLidoArrayAdapter extends ArrayAdapter<ItemCodigoBarraLido> {

    Context context;
    List<ItemCodigoBarraLido> objects;

    public ItemCodigoBarraLidoArrayAdapter(@NonNull Context context, @NonNull List<ItemCodigoBarraLido> objects) {
        super(context, 0, objects);
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_codigo_barra_lido, parent, false);
        }

        ItemCodigoBarraLido item = objects.get(position);

        TextView txtCodigoLido = convertView.findViewById(R.id.txtCodigoLido);
        TextView txtStatus = convertView.findViewById(R.id.txtStatus);

        txtCodigoLido.setText(item.getCodigo());
        txtStatus.setText(item.getStatus().getValue());

        if (item.getStatus() == ItemCodigoBarraLido.Status.ENCONTRADO) {
            txtStatus.setTextColor(Color.GREEN);
        }

        return convertView;
    }
}
