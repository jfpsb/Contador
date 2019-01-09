package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vandamodaintima.jfpsb.contador.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vandamodaintima.jfpsb.contador.view.ActivityBaseView.LOG;

public class ContagemCursorAdapter extends CursorAdapter {
    private SimpleDateFormat dateParaDataSQLite = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dataParaDisplay = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public ContagemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_pesquisa_contagem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView labelLojaNome = view.findViewById(R.id.labelLojaNome);
        TextView labelDataCriada = view.findViewById(R.id.labelDataCriada);
        CheckBox chkFinalizada = view.findViewById(R.id.chkFinalizada);

        String loja = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        String data = cursor.getString(cursor.getColumnIndexOrThrow("data"));
        int finalizada = cursor.getInt(cursor.getColumnIndexOrThrow("finalizada"));

        Date data2 = null;
        try {
            data2 = dateParaDataSQLite.parse(data);
        } catch (ParseException e) {
            Log.e(LOG, e.getMessage(), e);
        }

        labelLojaNome.setText(loja);
        labelDataCriada.setText(dataParaDisplay.format(data2));

        if (finalizada > 0) {
            chkFinalizada.setChecked(true);
        } else {
            chkFinalizada.setChecked(false);
        }
    }
}