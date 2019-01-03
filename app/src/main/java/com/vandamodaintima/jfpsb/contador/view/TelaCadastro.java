package com.vandamodaintima.jfpsb.contador.view;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.tela.DatePickerFragment;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class TelaCadastro extends Fragment implements CadastrarView {
    protected View view;
    protected SQLiteDatabase sqLiteDatabase;

    @Override
    public void onDestroy() {
        sqLiteDatabase.close();
        super.onDestroy();
    }

    @Override
    public void limparCampos() {
        try {
            throw new Exception("Sobrescreva na fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    protected void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
