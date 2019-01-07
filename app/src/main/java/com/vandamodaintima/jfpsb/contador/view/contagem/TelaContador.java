package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.DatePickerFragment;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutActivityBase;

public class TelaContador extends TabLayoutActivityBase {

    private CadastrarContagem cadastrarContagem;
    private PesquisarContagem pesquisarContagem;

    public TelaContador() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        cadastrarContagem = new CadastrarContagem();
        pesquisarContagem = new PesquisarContagem();

        setViewPagerTabLayout(pesquisarContagem, cadastrarContagem);
    }

    public void showDatePicker(View v) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setView(v);
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
