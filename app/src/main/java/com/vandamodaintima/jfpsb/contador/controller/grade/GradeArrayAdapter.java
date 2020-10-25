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
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesquisa_grade, parent, false);
        }

        final TextView txtTipoGrade = convertView.findViewById(R.id.txtTipoGrade);
        final TextView txtNomeGrade = convertView.findViewById(R.id.txtNomeGrade);
        txtTipoGrade.setText(objects.get(position).getTipoGrade().getNome());
        txtNomeGrade.setText(objects.get(position).getNome());
        return convertView;
    }

    public List<Grade> getObjects() {
        return objects;
    }
}
