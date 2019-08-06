package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;

public interface CadastrarView {
    void limparCampos();
    void mensagemAoUsuario(String mensagem);
    void aposCadastro(Object... args);
    void focoEmViewInicial();
    Context getContext();
}
