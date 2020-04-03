package com.vandamodaintima.jfpsb.contador.view.operadoracartao;

import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

public class CadastrarOperadoraCartao extends TelaCadastro {
    private EditText txtNome;



    @Override
    public void limparCampos() {
        txtNome.getText().clear();
    }

    @Override
    public void focoEmViewInicial() {

    }
}
