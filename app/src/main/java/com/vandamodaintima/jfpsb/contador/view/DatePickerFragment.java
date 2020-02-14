package com.vandamodaintima.jfpsb.contador.view;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private View v = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        try {
            if(v == null)
                throw new NullPointerException("Você Esqueceu de Chamar setView");

            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                editText.setText(dayOfMonth + "/" + (++month) + "/" + year);
            }
        }
        catch (NullPointerException e) {
            Toast.makeText(getContext(), "Erro ao Selecionar Data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Contador", e.getMessage(), e);
        }
    }

    public void setView(View v) {
        this.v = v;
    }
}
