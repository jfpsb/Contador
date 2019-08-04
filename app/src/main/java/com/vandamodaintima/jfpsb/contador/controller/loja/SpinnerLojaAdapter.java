package com.vandamodaintima.jfpsb.contador.controller.loja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerLojaAdapter extends ArrayAdapter<Loja> {

    private List<Loja> objects;
    private Context context;

    public SpinnerLojaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Loja> objects) {
        super(context, resource, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        final TextView label=(TextView)layout.findViewById(android.R.id.text1);
        label.setText(objects.get(position).getNome());
        return layout;
    }
}
