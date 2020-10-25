package com.vandamodaintima.jfpsb.contador.controller.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.Loja;

import java.util.List;

public class SpinnerGradeAdapter extends ArrayAdapter<Grade> {

    private List<Grade> objects;

    public SpinnerGradeAdapter(@NonNull Context context, List<Grade> objects) {
        super(context, 0, objects);
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public void clear() {
        super.clear();
        objects.clear();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        final TextView label = layout.findViewById(android.R.id.text1);
        label.setText(objects.get(position).getNome());
        return layout;
    }
}
