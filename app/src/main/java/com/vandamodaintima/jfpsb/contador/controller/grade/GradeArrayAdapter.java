package com.vandamodaintima.jfpsb.contador.controller.grade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;

import java.util.List;

public class GradeArrayAdapter extends ArrayAdapter<Grade> {
    private List<Grade> objects;

    public GradeArrayAdapter(@NonNull Context context, @NonNull List<Grade> objects) {
        super(context, 0, objects);
        setNotifyOnChange(true);
        this.objects = objects;
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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesquisa_grade, parent, false);
        final TextView txtTipoGrade = layout.findViewById(R.id.txtTipoGrade);
        final TextView txtNomeGrade = layout.findViewById(R.id.txtNomeGrade);
        txtTipoGrade.setText(objects.get(position).getTipoGrade().getNome());
        txtNomeGrade.setText(objects.get(position).getNome());
        return layout;
    }

    public List<Grade> getObjects() {
        return objects;
    }
}
