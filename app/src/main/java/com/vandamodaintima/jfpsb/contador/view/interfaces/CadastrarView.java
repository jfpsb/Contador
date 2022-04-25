package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.ListAdapter;

public interface CadastrarView {
    void limparCampos();
    void mensagemAoUsuario(String mensagem);
    void aposCadastro(Object... args);
    void focoEmViewInicial();
    void setListViewAdapter(ListAdapter adapter);
    Context getContext();
}
