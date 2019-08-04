package com.vandamodaintima.jfpsb.contador.view;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.PesquisarView;

public abstract class TelaCadastro extends Fragment implements CadastrarView {
    protected View view;
    protected ConexaoBanco conexaoBanco;

    @Override
    public void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {
        Fragment fragment = null;

        if(getActivity() != null)
            fragment = getActivity().getSupportFragmentManager().getFragments().get(0);

        if (fragment instanceof PesquisarView)
            ((PesquisarView) fragment).realizarPesquisa();

        focoEmViewInicial();
    }

    protected void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
