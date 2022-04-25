package com.vandamodaintima.jfpsb.contador.view;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public abstract class TelaCadastro extends Fragment implements CadastrarView {
    protected View telaCadastroView;
    protected ConexaoBanco conexaoBanco;

    @Override
    public void onDestroy() {
        if (conexaoBanco != null)
            conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aposCadastro(Object... args) {
        /*if (getActivity() != null) {
            for(Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                if (fragment instanceof PesquisarView)
                    ((PesquisarView) fragment).realizarPesquisa();
            }
        }*/

        limparCampos();
        focoEmViewInicial();
    }

    protected void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }
}
